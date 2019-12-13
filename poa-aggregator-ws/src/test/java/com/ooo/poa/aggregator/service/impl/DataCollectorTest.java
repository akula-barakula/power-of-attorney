package com.ooo.poa.aggregator.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.aggregator.service.impl.DataCollector.CollectedData;
import com.ooo.poa.aggregator.ws.model.ModelTest;
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.Authorization;
import com.ooo.poa.client.model.CardType;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.Status;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataCollectorTest extends ModelTest {

    @Configuration
    public static class DataCollectorTestConfig {

        @Bean
        public PoaClient poaClient() {
            return mock(PoaClient.class);
        }

        @Bean
        public DataCollector dataCollector() {
            return new DataCollector();
        }
    }


    @Autowired
    private PoaClient poaClient;

    @Autowired
    private DataCollector dataCollector;


    @Before
    public void before() {
        reset(poaClient);


        PowerOfAttorney newPoa = newPoa("poa1", "tor", "tee", "NL00RABOacc1")
                .addCardsItem(newCardReference("credit1", CardType.CREDIT_CARD))
                .addCardsItem(newCardReference("debit1", CardType.DEBIT_CARD));

        when(poaClient.getAllPowerOfAttorneyReferences())
                .thenReturn(Arrays.asList(newPoaReference("ref1")));
        when(poaClient.getPowerOfAttorney("ref1"))
                .thenReturn(newPoa);
        when(poaClient.getAccount("acc1"))
                .thenReturn(newAccount("acc1"));
        when(poaClient.getCreditCard("credit1"))
                .thenReturn(newCreditCard("credit1"));
        when(poaClient.getDebitCard("debit1"))
                .thenReturn(newDebitCard("debit1"));
    }


    @Test
    public void collectFor_delegator() {
        CollectedData data = dataCollector.collectFor("tor");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(1, creditCards.size());
        assertEquals(1, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("credit1", creditCards.get("credit1").getId());
        assertEquals("debit1", debitCards.get("debit1").getId());
    }

    @Test
    public void collectFor_delegatee() {
        CollectedData data = dataCollector.collectFor("tee");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(1, creditCards.size());
        assertEquals(1, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("credit1", creditCards.get("credit1").getId());
        assertEquals("debit1", debitCards.get("debit1").getId());
    }

    @Test
    public void collectFor_noUserMatch() {
        CollectedData data = dataCollector.collectFor("user");
        assertEquals(0, data.getPowerOfAttorneys().size());
        assertEquals(0, data.getAccounts().size());
        assertEquals(0, data.getCreditCards().size());
        assertEquals(0, data.getDebitCards().size());
    }

    @Test
    public void collectFor_noViewAuthorizationForGrantee() {
        PowerOfAttorney newPoa = newPoa("poa1", "tor", "tee", "NL00RABOacc1")
                .authorizations(Arrays.asList(Authorization.PAYMENT, Authorization.CREDIT_CARD, Authorization.DEBIT_CARD))
                .addCardsItem(newCardReference("credit1", CardType.CREDIT_CARD))
                .addCardsItem(newCardReference("debit1", CardType.DEBIT_CARD));
        when(poaClient.getPowerOfAttorney("ref1"))
                .thenReturn(newPoa);

        CollectedData data = dataCollector.collectFor("tee");
        assertEquals(0, data.getPowerOfAttorneys().size());
        assertEquals(0, data.getAccounts().size());
        assertEquals(0, data.getCreditCards().size());
        assertEquals(0, data.getDebitCards().size());
    }

    @Test
    public void collectFor_accountIsEnded() {
        when(poaClient.getAccount("acc1"))
                .thenReturn(newAccount("acc1").ended("11-11-1111"));


        CollectedData data = dataCollector.collectFor("tor");
        assertEquals(0, data.getPowerOfAttorneys().size());
        assertEquals(1, data.getAccounts().size());
        assertEquals("acc1", data.getAccount("NL00RABOacc1").getNumber());
        assertEquals(0, data.getCreditCards().size());
        assertEquals(0, data.getDebitCards().size());
    }

    @Test
    public void collectFor_noCreditCardAuthorization() {
        PowerOfAttorney newPoa = newPoa("poa1", "tor", "tee", "NL00RABOacc1")
                .authorizations(Arrays.asList(Authorization.VIEW, Authorization.PAYMENT, Authorization.DEBIT_CARD))
                .addCardsItem(newCardReference("credit1", CardType.CREDIT_CARD))
                .addCardsItem(newCardReference("debit1", CardType.DEBIT_CARD));
        when(poaClient.getPowerOfAttorney("ref1"))
                .thenReturn(newPoa);


        CollectedData data = dataCollector.collectFor("tor");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(0, creditCards.size());
        assertEquals(1, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("debit1", debitCards.get("debit1").getId());
    }

    @Test
    public void collectFor_noDebitCardAuthorization() {
        PowerOfAttorney newPoa = newPoa("poa1", "tor", "tee", "NL00RABOacc1")
                .authorizations(Arrays.asList(Authorization.VIEW, Authorization.PAYMENT, Authorization.CREDIT_CARD))
                .addCardsItem(newCardReference("credit1", CardType.CREDIT_CARD))
                .addCardsItem(newCardReference("debit1", CardType.DEBIT_CARD));
        when(poaClient.getPowerOfAttorney("ref1"))
                .thenReturn(newPoa);


        CollectedData data = dataCollector.collectFor("tor");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(1, creditCards.size());
        assertEquals(0, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("credit1", creditCards.get("credit1").getId());
    }

    @Test
    public void collectFor_blockedCreditCard() {
        when(poaClient.getCreditCard("credit1"))
                .thenReturn(newCreditCard("credit1").status(Status.BLOCKED));


        CollectedData data = dataCollector.collectFor("tor");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(0, creditCards.size());
        assertEquals(1, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("debit1", debitCards.get("debit1").getId());
    }

    @Test
    public void collectFor_blockedDebitCard() {
        when(poaClient.getDebitCard("debit1"))
                .thenReturn(newDebitCard("debit1").status(Status.BLOCKED));


        CollectedData data = dataCollector.collectFor("tor");
        List<PowerOfAttorney> poas = data.getPowerOfAttorneys();
        Map<String, Account> accounts = data.getAccounts();
        Map<String, CreditCard> creditCards = data.getCreditCards();
        Map<String, DebitCard> debitCards = data.getDebitCards();
        assertEquals(1, poas.size());
        assertEquals(1, accounts.size());
        assertEquals(1, creditCards.size());
        assertEquals(0, debitCards.size());

        assertEquals("poa1", poas.get(0).getId());
        assertEquals("acc1", accounts.get("NL00RABOacc1").getNumber());
        assertEquals("credit1", creditCards.get("credit1").getId());
    }
}
