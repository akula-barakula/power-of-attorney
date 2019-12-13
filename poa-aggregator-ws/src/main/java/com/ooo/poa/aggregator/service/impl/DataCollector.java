package com.ooo.poa.aggregator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.aggregator.utils.Utils;
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.Authorization;
import com.ooo.poa.client.model.CardReference;
import com.ooo.poa.client.model.CardType;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.PowerOfAttorneyReference;

public class DataCollector {

    @Autowired
    private PoaClient poaClient;


    public CollectedData collect() {

        List<PowerOfAttorneyReference> poaIds = poaClient.getAllPowerOfAttorneyReferences();

        List<PowerOfAttorney> powerOfAttorneys = new ArrayList<>();
        for (PowerOfAttorneyReference poaId : poaIds) {
            PowerOfAttorney poa = poaClient.getPowerOfAttorney(poaId.getId());

            powerOfAttorneys.add(poa);
        }

        Map<String, Account> accounts = new HashMap<>();
        Map<String, CreditCard> creditCards = new HashMap<>();
        Map<String, DebitCard> debitCards = new HashMap<>();
        for (PowerOfAttorney poa : powerOfAttorneys) {
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

        return new CollectedData(powerOfAttorneys, accounts, creditCards, debitCards);
    }


    public static class CollectedData {

        private final List<PowerOfAttorney> powerOfAttorneys;
        private final Map<String, Account> accounts;
        private final Map<String, CreditCard> creditCards;
        private final Map<String, DebitCard> debitCards;


        public CollectedData(
                List<PowerOfAttorney> powerOfAttorneys,
                Map<String, Account> accounts,
                Map<String, CreditCard> creditCards,
                Map<String, DebitCard> debitCards) {

            this.powerOfAttorneys = powerOfAttorneys;
            this.accounts = accounts;
            this.creditCards = creditCards;
            this.debitCards = debitCards;
        }


        public List<PowerOfAttorney> getPowerOfAttorneys() {
            return powerOfAttorneys;
        }

        public Map<String, Account> getAccounts() {
            return accounts;
        }

        public Map<String, CreditCard> getCreditCards() {
            return creditCards;
        }

        public Map<String, DebitCard> getDebitCards() {
            return debitCards;
        }
    }
}
