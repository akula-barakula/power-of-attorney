package com.ooo.poa.aggregator.ws.model;

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

public abstract class ModelTest {

    public PowerOfAttorneyReference newPoaReference(String id) {
        return new PowerOfAttorneyReference().id(id);
    }

    public PowerOfAttorney newPoa(
            String id,
            String grantor,
            String grantee,
            String accountIban) {

        return new PowerOfAttorney()
                .id(id)
                .grantor(grantor)
                .direction(Direction.GIVEN)
                .grantee(grantee)
                .addAuthorizationsItem(Authorization.VIEW)
                .addAuthorizationsItem(Authorization.PAYMENT)
                .addAuthorizationsItem(Authorization.CREDIT_CARD)
                .addAuthorizationsItem(Authorization.DEBIT_CARD)
                .account(accountIban);
    }

    public Account newAccount(String number) {
        return new Account().number(number);
    }

    public CardReference newCardReference(
            String id,
            CardType cardType) {

        return new CardReference()
                .id(id)
                .type(cardType);
    }

    public CreditCard newCreditCard(String id) {
        return new CreditCard()
                .id(id)
                .status(Status.ACTIVE);
    }

    public DebitCard newDebitCard(String id) {
        return new DebitCard()
                .id(id)
                .status(Status.ACTIVE);
    }


    public PowerOfAttorneyAggr newPoaAggr(String id) {

        PowerOfAttorneyAggr result = new PowerOfAttorneyAggr();

        result.setId(id);

        return result;
    }
}
