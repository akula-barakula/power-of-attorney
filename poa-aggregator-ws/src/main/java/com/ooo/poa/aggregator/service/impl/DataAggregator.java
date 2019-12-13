package com.ooo.poa.aggregator.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ooo.poa.aggregator.ws.model.AccountAggr;
import com.ooo.poa.aggregator.ws.model.AuthorizationAggr;
import com.ooo.poa.aggregator.ws.model.CreditCardAggr;
import com.ooo.poa.aggregator.ws.model.DebitCardAggr;
import com.ooo.poa.aggregator.ws.model.DirectionAggr;
import com.ooo.poa.aggregator.ws.model.LimitAggr;
import com.ooo.poa.aggregator.ws.model.PeriodUnitAggr;
import com.ooo.poa.aggregator.ws.model.PowerOfAttorneyAggr;
import com.ooo.poa.aggregator.ws.model.StatusAggr;
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
import com.ooo.poa.client.model.Status;

public class DataAggregator {

    public List<PowerOfAttorneyAggr> aggregate(
            List<PowerOfAttorney> powerOfAttorneys,
            Map<String, Account> accounts,
            Map<String, CreditCard> creditCards,
            Map<String, DebitCard> debitCards) {

        return powerOfAttorneys
            .stream()
            .map(powerOfAttorney -> toAggregated(powerOfAttorney, accounts, creditCards, debitCards))
            .collect(Collectors.toList());
    }

    private PowerOfAttorneyAggr toAggregated(
            PowerOfAttorney powerOfAttorney,
            Map<String, Account> accounts,
            Map<String, CreditCard> creditCards,
            Map<String, DebitCard> debitCards) {

        PowerOfAttorneyAggr result = new PowerOfAttorneyAggr();

        result.setId(powerOfAttorney.getId());
        result.setGrantor(powerOfAttorney.getGrantor());
        result.setGrantee(powerOfAttorney.getGrantee());
        result.setAccount(toAccountAggr(powerOfAttorney.getAccount(), accounts));
        result.setDirection(toDirectionAggr(powerOfAttorney.getDirection()));
        result.setAuthorizations(toAuthorizations(powerOfAttorney.getAuthorizations()));
        result.setCreditCards(toCreditCardAggrs(powerOfAttorney.getCards(), creditCards));
        result.setDebitCards(toDebitCardAggrs(powerOfAttorney.getCards(), debitCards));

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

    private List<AuthorizationAggr> toAuthorizations(List<Authorization> authorizations) {

        if (CollectionUtils.isEmpty(authorizations)) {
            return null;
        }

        return authorizations
                .stream()
                .map(authorization -> toAuthorizationAggr(authorization))
                .collect(Collectors.toList());
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
}
