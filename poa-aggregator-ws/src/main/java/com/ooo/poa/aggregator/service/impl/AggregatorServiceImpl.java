package com.ooo.poa.aggregator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.aggregator.utils.Utils;
import com.ooo.poa.aggregator.ws.api.AccountAggr;
import com.ooo.poa.aggregator.ws.api.AuthorizationAggr;
import com.ooo.poa.aggregator.ws.api.CreditCardAggr;
import com.ooo.poa.aggregator.ws.api.DebitCardAggr;
import com.ooo.poa.aggregator.ws.api.DirectionAggr;
import com.ooo.poa.aggregator.ws.api.LimitAggr;
import com.ooo.poa.aggregator.ws.api.PeriodUnitAggr;
import com.ooo.poa.aggregator.ws.api.PowerOfAttorneyAggr;
import com.ooo.poa.aggregator.ws.api.StatusAggr;
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.Authorization;
import com.ooo.poa.client.model.CardReference;
import com.ooo.poa.client.model.CardType;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.Direction;
import com.ooo.poa.client.model.Limit;
import com.ooo.poa.client.model.PeriodUnit;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.PowerOfAttorneyReference;
import com.ooo.poa.client.model.Status;

// TODO OOO finish filtering logic
public class AggregatorServiceImpl implements AggregatorService {

    @Autowired
    private PoaClient poaClient;


	@Override
	public List<PowerOfAttorneyAggr> getPowerOfAttorneys() {

		List<PowerOfAttorneyReference> poaIds = poaClient.getAllPowerOfAttorneyReferences();

		List<PowerOfAttorney> poas = new ArrayList<>();
		for (PowerOfAttorneyReference poaId : poaIds) {
			PowerOfAttorney poa = poaClient.getPowerOfAttorney(poaId.getId());

			poas.add(poa);
		}

		Map<String, Account> accounts = new HashMap<>();
		Map<String, CreditCard> creditCards = new HashMap<>();
		Map<String, DebitCard> debitCards = new HashMap<>();
		for (PowerOfAttorney poa : poas) {
			String accountIban = poa.getAccount();

			Account account = poaClient.getAccount(Utils.toAccountNumber(accountIban));
			accounts.put(accountIban, account);

			List<Authorization> authorizations = poa.getAuthorizations();
			boolean canAccessCreditCard = authorizations.contains(Authorization.CREDIT_CARD);
			boolean canAccessDebitCard = authorizations.contains(Authorization.DEBIT_CARD);

			if (CollectionUtils.isEmpty(poa.getCards())) {
				continue;
			}

			for (CardReference cardReference : poa.getCards()) {
				String cardId = cardReference.getId();

				if (cardReference.getType() == CardType.CREDIT_CARD) {
					if (canAccessCreditCard
							&& !creditCards.containsKey(cardId)) {
						CreditCard card = poaClient.getCreditCard(cardId);

						creditCards.put(cardId, card);
					}

				} else if (cardReference.getType() == CardType.DEBIT_CARD) {
					if (canAccessDebitCard
							&& !debitCards.containsKey(cardId)) {
						DebitCard card = poaClient.getDebitCard(cardId);

						debitCards.put(cardId, card);
					}
				}
			}
		}

		return poas
				.stream()
				.map(poa -> toAggregated(poa, accounts, creditCards, debitCards))
				.collect(Collectors.toList());
	}

	private PowerOfAttorneyAggr toAggregated(
			PowerOfAttorney poa,
			Map<String, Account> accounts,
			Map<String, CreditCard> creditCards,
			Map<String, DebitCard> debitCards) {

		PowerOfAttorneyAggr result = new PowerOfAttorneyAggr();

		result.setId(poa.getId());
		result.setGrantor(poa.getGrantor());
		result.setGrantee(poa.getGrantee());
		result.setAccount(toAccountAggr(poa.getAccount(), accounts));
		result.setDirection(toDirectionAggr(poa.getDirection()));
		result.setAuthorizations(toAuthorizations(poa.getAuthorizations()));
		result.setCreditCards(toCreditCardAggrs(poa.getCards(), creditCards));
		result.setDebitCards(toDebitCardAggrs(poa.getCards(), debitCards));

		return result;
	}

	private AccountAggr toAccountAggr(
			String accountIban,
			Map<String, Account> accounts) {

		Account account = accounts.get(accountIban);

		if (account == null) {
			return null;
		}

		AccountAggr result = new AccountAggr();

		result.setNumber(account.getNumber());
		result.setIban(account.getIban());
		result.setOwner(account.getOwner());
		result.setBalance(account.getBalance());
		result.setCreated(account.getCreated());
		result.setEnded(account.getEnded());

		return result;
	}

	private DirectionAggr toDirectionAggr(Direction direction) {

		switch (direction) {
		case GIVEN:
		    return DirectionAggr.GIVEN;

		case RECEIVED:
		    return DirectionAggr.RECEIVED;

        default:
        	return null;
		}
	}

	private List<AuthorizationAggr> toAuthorizations(List<Authorization> authorizations) {

		if (CollectionUtils.isEmpty(authorizations)) {
			return null;
		}

		return authorizations
				.stream()
				.map(authorization -> toAuthorizationAggr(authorization))
				.collect(Collectors.toList());
	}

	private AuthorizationAggr toAuthorizationAggr(Authorization authorization) {

		switch (authorization) {
		case DEBIT_CARD:
		    return AuthorizationAggr.DEBIT_CARD;

		case CREDIT_CARD:
		    return AuthorizationAggr.CREDIT_CARD;

		case VIEW:
		    return AuthorizationAggr.VIEW;

		case PAYMENT:
		    return AuthorizationAggr.PAYMENT;

        default:
        	return null;
		}
	}

	private List<CreditCardAggr> toCreditCardAggrs(
			List<CardReference> cardReferences,
			Map<String, CreditCard> creditCards) {

		if (CollectionUtils.isEmpty(cardReferences)) {
			return null;
		}

		return cardReferences
				.stream()
				.filter(cardReference -> cardReference.getType() == CardType.CREDIT_CARD)
				.map(cardReference -> toCreditCardArg(creditCards.get(cardReference.getId())))
				.filter(creditCard -> creditCard != null)
				.collect(Collectors.toList());
	}

	private CreditCardAggr toCreditCardArg(CreditCard creditCard) {

		if (creditCard == null) {
			return null;
		}

		CreditCardAggr result = new CreditCardAggr();

		result.setId(creditCard.getId());
		result.setStatus(toStatusAggr(creditCard.getStatus()));
		result.setCardNumber(creditCard.getCardNumber());
		result.setSequenceNumber(creditCard.getSequenceNumber());
		result.setCardHolder(creditCard.getCardHolder());
		result.setMonhtlyLimit(creditCard.getMonhtlyLimit());

		return result;
	}

	private StatusAggr toStatusAggr(Status status) {

		switch (status) {
		case ACTIVE:
		    return StatusAggr.ACTIVE;

		case BLOCKED:
		    return StatusAggr.BLOCKED;

        default:
        	return null;
		}
	}

	private List<DebitCardAggr> toDebitCardAggrs(
			List<CardReference> cardReferences,
			Map<String, DebitCard> debitCards) {

		if (CollectionUtils.isEmpty(cardReferences)) {
			return null;
		}

		return cardReferences
				.stream()
				.filter(cardReference -> cardReference.getType() == CardType.DEBIT_CARD)
				.map(cardReference -> toDebitCardArg(debitCards.get(cardReference.getId())))
				.filter(debitCard -> debitCard != null)
				.collect(Collectors.toList());
	}

	private DebitCardAggr toDebitCardArg(DebitCard debitCard) {

		if (debitCard == null) {
			return null;
		}

		DebitCardAggr result = new DebitCardAggr();

		result.setId(debitCard.getId());
		result.setStatus(toStatusAggr(debitCard.getStatus()));
		result.setCardNumber(debitCard.getCardNumber());
		result.setSequenceNumber(debitCard.getSequenceNumber());
		result.setCardHolder(debitCard.getCardHolder());
		result.setAtmLimit(toLimitAggr(debitCard.getAtmLimit()));
		result.setPosLimit(toLimitAggr(debitCard.getPosLimit()));
		result.setContactless(debitCard.isContactless());

		return result;
	}

	private LimitAggr toLimitAggr(Limit limit) {

		if (limit == null) {
			return null;
		}

		LimitAggr result = new LimitAggr();

		result.setLimit(limit.getLimit());
		result.setPeriodUnit(toPeriodUnitAggr(limit.getPeriodUnit()));

		return result;
	}

	private PeriodUnitAggr toPeriodUnitAggr(PeriodUnit periodUnit) {

		switch (periodUnit) {
		case DAY:
		    return PeriodUnitAggr.DAY;

		case WEEK:
		    return PeriodUnitAggr.WEEK;

		case MONTH:
		    return PeriodUnitAggr.MONTH;

        default:
        	return null;
		}
	}
}
