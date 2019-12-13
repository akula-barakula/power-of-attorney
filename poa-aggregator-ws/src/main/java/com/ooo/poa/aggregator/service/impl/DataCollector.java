package com.ooo.poa.aggregator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ooo.poa.client.model.Direction;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.PowerOfAttorneyReference;
import com.ooo.poa.client.model.Status;

/**
 *
 * Collects and filters data.
 *
 */
public class DataCollector {

    @Autowired
    private PoaClient poaClient;


    public CollectedData collectFor(String user) {

        CollectedData result = new CollectedData();

        for (PowerOfAttorneyReference poaReference : poaClient.getAllPowerOfAttorneyReferences()) {
            collectFor(user, poaReference, result);
        }

        return result;
    }

    private void collectFor(
            String user,
            PowerOfAttorneyReference poaReference,
            CollectedData collectedData) {

        PowerOfAttorney powerOfAttorney = poaClient.getPowerOfAttorney(poaReference.getId());
        /*
         * we have requirement: Only show data that a user is actually authorized for
         *     that is why if user does not have VIEW authorization then poa will not be shown
         */
        if (!isAuthorizedFor(user, powerOfAttorney)) {
            return;
        }

        Account account = getAccount(powerOfAttorney, collectedData);
        /*
         * we have requirement: Don't return inactive products or accounts
         *     that is why poa for ended account will not be shown
         */
        if (!isActive(account)) {
            return;
        }

        collectedData.addPowerOfAttorney(powerOfAttorney);
        collectCardDataFrom(powerOfAttorney, collectedData);
    }

    private boolean isAuthorizedFor(
            String user,
            PowerOfAttorney powerOfAttorney) {

        Direction direction = powerOfAttorney.getDirection();
        String from = (direction == Direction.GIVEN) ? powerOfAttorney.getGrantor() : powerOfAttorney.getGrantee();

        if (user.equalsIgnoreCase(from)) {
            return true;
        }

        String to = (direction == Direction.GIVEN) ? powerOfAttorney.getGrantee() : powerOfAttorney.getGrantor();

        return user.equalsIgnoreCase(to)
                && powerOfAttorney.getAuthorizations().contains(Authorization.VIEW);
    }

    private Account getAccount(
            PowerOfAttorney powerOfAttorney,
            CollectedData collectedData) {

        String accountIban = powerOfAttorney.getAccount();
        if (collectedData.containsAccount(accountIban)) {
            return collectedData.getAccount(accountIban);
        }

        String accountNumber = Utils.toAccountNumber(accountIban);
        Account result = poaClient.getAccount(accountNumber);

        collectedData.addAccount(accountIban, result);

        return result;
    }

    /**
     * ended - when account was ended/closed:
     *         == null - was not closed yet
     *         != null - is already closed
     */
    private boolean isActive(Account account) {
        return account.getEnded() == null;
    }

    private void collectCardDataFrom(
            PowerOfAttorney powerOfAttorney,
            CollectedData collectedData) {

        if (CollectionUtils.isEmpty(powerOfAttorney.getCards())) {
            return;
        }

        List<Authorization> authorizations = powerOfAttorney.getAuthorizations();
        boolean showCreditCards = authorizations.contains(Authorization.CREDIT_CARD);
        boolean showDebitCards = authorizations.contains(Authorization.DEBIT_CARD);
        List<CardReference> cardsToHide = new ArrayList<>();

        for (CardReference cardReference : powerOfAttorney.getCards()) {
            boolean hideCard = collectCardData(cardReference, showCreditCards, showDebitCards, collectedData);

            if (hideCard) {
                cardsToHide.add(cardReference);
            }
        }

        powerOfAttorney.getCards().removeAll(cardsToHide);
    }

    private boolean collectCardData(
            CardReference cardReference,
            boolean showCreditCards,
            boolean showDebitCards,
            CollectedData collectedData) {

        if (cardReference.getType() == CardType.CREDIT_CARD) {
            return collectCreditCardData(showCreditCards, cardReference.getId(), collectedData);

        } else {
            return collectDebitCardData(showDebitCards, cardReference.getId(), collectedData);
        }
    }

    /**
     * we have requirement: Only show data that a user is actually authorized for
     *     that is why if user does not have authorization for CREDIT_CARD/DEBIT_CARD
     *     then they will not be shown
     *
     * we have requirement: Don't return inactive products or accounts
     *     that is why blocked cards will not be shown
     */
    private boolean collectCreditCardData(
            boolean showCreditCards,
            String cardId,
            CollectedData collectedData) {

        if (!showCreditCards
                || collectedData.containsBlockedCard(cardId)) {
            return true;

        } else if (collectedData.containsCreditCard(cardId)) {
            return false;
        }

        CreditCard card = poaClient.getCreditCard(cardId);

        if (card.getStatus() == Status.BLOCKED) {
            collectedData.addBlockedCard(cardId);

            return true;
        }

        collectedData.addCreditCard(cardId, card);

        return false;
    }

    /**
     * we have requirement: Only show data that a user is actually authorized for
     *     that is why if user does not have authorization for CREDIT_CARD/DEBIT_CARD
     *     then they will not be shown
     *
     * we have requirement: Don't return inactive products or accounts
     *     that is why blocked cards will not be shown
     */
    private boolean collectDebitCardData(
            boolean showDebitCards,
            String cardId,
            CollectedData collectedData) {

        if (!showDebitCards
                || collectedData.containsBlockedCard(cardId)) {
            return true;

        } else if (collectedData.containsDebitCard(cardId)) {
            return false;
        }

        DebitCard card = poaClient.getDebitCard(cardId);

        if (card.getStatus() == Status.BLOCKED) {
            collectedData.addBlockedCard(cardId);

            return true;
        }

        collectedData.addDebitCard(cardId, card);

        return false;
    }


    public static class CollectedData {

        private final List<PowerOfAttorney> powerOfAttorneys = new ArrayList<>();
        private final Map<String, Account> accounts = new HashMap<>();

        private final Map<String, CreditCard> creditCards = new HashMap<>();
        private final Map<String, DebitCard> debitCards = new HashMap<>();
        private final Set<String> blockedCards = new HashSet<>();


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


        public void addPowerOfAttorney(PowerOfAttorney powerOfAttorney) {
            powerOfAttorneys.add(powerOfAttorney);
        }

        public boolean containsAccount(String accountIban) {
            return accounts.containsKey(accountIban);
        }

        public Account getAccount(String accountIban) {
            return accounts.get(accountIban);
        }

        public void addAccount(
                String accountIban,
                Account account) {
            accounts.put(accountIban, account);
        }

        public boolean containsCreditCard(String cardId) {
            return creditCards.containsKey(cardId);
        }

        public void addCreditCard(
                String cardId,
                CreditCard card) {
            creditCards.put(cardId, card);
        }

        public boolean containsDebitCard(String cardId) {
            return debitCards.containsKey(cardId);
        }

        public void addDebitCard(
                String cardId,
                DebitCard card) {
            debitCards.put(cardId, card);
        }

        public boolean containsBlockedCard(String cardId) {
            return blockedCards.contains(cardId);
        }

        public void addBlockedCard(String cardId) {
            blockedCards.add(cardId);
        }
    }
}
