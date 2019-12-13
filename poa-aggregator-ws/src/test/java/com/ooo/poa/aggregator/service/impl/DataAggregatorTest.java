package com.ooo.poa.aggregator.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ooo.poa.aggregator.ws.model.AuthorizationAggr;
import com.ooo.poa.aggregator.ws.model.CreditCardAggr;
import com.ooo.poa.aggregator.ws.model.DebitCardAggr;
import com.ooo.poa.aggregator.ws.model.DirectionAggr;
import com.ooo.poa.aggregator.ws.model.ModelTest;
import com.ooo.poa.aggregator.ws.model.PeriodUnitAggr;
import com.ooo.poa.aggregator.ws.model.PowerOfAttorneyAggr;
import com.ooo.poa.aggregator.ws.model.StatusAggr;
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.CardType;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PeriodUnit;
import com.ooo.poa.client.model.PowerOfAttorney;

public class DataAggregatorTest extends ModelTest {

    private DataAggregator dataAggregator;
    private List<PowerOfAttorney> powerOfAttorneys;
    private Map<String, Account> accounts;
    private Map<String, CreditCard> creditCards;
    private Map<String, DebitCard> debitCards;


    @Before
    public void before() {
        dataAggregator = new DataAggregator();
        powerOfAttorneys = new ArrayList<>();
        accounts = new HashMap<>();
        creditCards = new HashMap<>();
        debitCards = new HashMap<>();
    }


    @Test
    public void aggregate_empty() {
        List<PowerOfAttorneyAggr> aggregates = dataAggregator.aggregate(powerOfAttorneys, accounts, creditCards, debitCards);

        assertEquals(0, aggregates.size());
    }

    @Test
    public void aggregate() {
        powerOfAttorneys.add(newPoa("poa1", "tor", "tee", "NL00RABOacc1")
                .addCardsItem(newCardReference("credit1", CardType.CREDIT_CARD))
                .addCardsItem(newCardReference("debit1", CardType.DEBIT_CARD)));
        accounts.put("NL00RABOacc1", newAccount("acc1").iban("NL00RABOacc1").owner("tor")
                .balance(10).created("11-11-1011").ended("11-11-2011"));
        creditCards.put("credit1", newCreditCard("credit1").cardNumber(10).sequenceNumber(5).cardHolder("Card Holder")
                .monhtlyLimit(100));
        debitCards.put("debit1", newDebitCard("debit1").cardNumber(11).sequenceNumber(6).cardHolder("Card Holder 2")
                .atmLimit(newLimit(20, PeriodUnit.DAY)).posLimit(newLimit(50, PeriodUnit.WEEK)).contactless(true));


        List<PowerOfAttorneyAggr> aggregates = dataAggregator.aggregate(powerOfAttorneys, accounts, creditCards, debitCards);
        assertEquals(1, aggregates.size());

        PowerOfAttorneyAggr poaAggr = aggregates.get(0);
        assertEquals("poa1", poaAggr.getId());
        assertEquals("tor", poaAggr.getGrantor());
        assertSame(DirectionAggr.GIVEN, poaAggr.getDirection());
        assertEquals("tee", poaAggr.getGrantee());
        assertEquals(Arrays.asList(AuthorizationAggr.VIEW, AuthorizationAggr.PAYMENT, AuthorizationAggr.CREDIT_CARD, AuthorizationAggr.DEBIT_CARD),
                poaAggr.getAuthorizations());

        assertEquals("NL00RABOacc1", poaAggr.getAccount().getIban());
        assertEquals("acc1", poaAggr.getAccount().getNumber());
        assertEquals("tor", poaAggr.getAccount().getOwner());
        assertEquals(Integer.valueOf(10), poaAggr.getAccount().getBalance());
        assertEquals("11-11-1011", poaAggr.getAccount().getCreated());
        assertEquals("11-11-2011", poaAggr.getAccount().getEnded());

        assertEquals(1, poaAggr.getCreditCards().size());
        CreditCardAggr creditCardAggr = poaAggr.getCreditCards().get(0);
        assertEquals("credit1", creditCardAggr.getId());
        assertEquals(Integer.valueOf(10), creditCardAggr.getCardNumber());
        assertEquals(Integer.valueOf(5), creditCardAggr.getSequenceNumber());
        assertEquals("Card Holder", creditCardAggr.getCardHolder());
        assertSame(StatusAggr.ACTIVE, creditCardAggr.getStatus());
        assertEquals(Integer.valueOf(100), creditCardAggr.getMonhtlyLimit());

        assertEquals(1, poaAggr.getDebitCards().size());
        DebitCardAggr debitCardAggr = poaAggr.getDebitCards().get(0);
        assertEquals("debit1", debitCardAggr.getId());
        assertEquals(Integer.valueOf(11), debitCardAggr.getCardNumber());
        assertEquals(Integer.valueOf(6), debitCardAggr.getSequenceNumber());
        assertEquals("Card Holder 2", debitCardAggr.getCardHolder());
        assertSame(StatusAggr.ACTIVE, debitCardAggr.getStatus());
        assertEquals(Integer.valueOf(20), debitCardAggr.getAtmLimit().getLimit());
        assertSame(PeriodUnitAggr.DAY, debitCardAggr.getAtmLimit().getPeriodUnit());
        assertEquals(Integer.valueOf(50), debitCardAggr.getPosLimit().getLimit());
        assertSame(PeriodUnitAggr.WEEK, debitCardAggr.getPosLimit().getPeriodUnit());
        assertSame(true, debitCardAggr.isContactless());
    }
}
