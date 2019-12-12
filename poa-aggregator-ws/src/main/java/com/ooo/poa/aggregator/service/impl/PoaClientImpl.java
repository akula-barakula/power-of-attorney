package com.ooo.poa.aggregator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.client.api.AccountApi;
import com.ooo.poa.client.api.CreditCardApi;
import com.ooo.poa.client.api.DebitCardApi;
import com.ooo.poa.client.api.PowerOfAttorneyApi;
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.PowerOfAttorneyReference;

public class PoaClientImpl implements PoaClient {

    @Autowired
    private PowerOfAttorneyApi powerOfAttorneyApi;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private CreditCardApi creditCardApi;

    @Autowired
    private DebitCardApi debitCardApi;


	@Override
	public List<PowerOfAttorneyReference> getAllPowerOfAttorneyReferences() {
		return powerOfAttorneyApi.getAllPowerOfAttorneyReferences();
	}

	@Override
	public PowerOfAttorney getPowerOfAttorney(String poaId) {
		return powerOfAttorneyApi.getPowerOfAttorneyDetail(poaId);
	}

	@Override
	public Account getAccount(String accountNumber) {
		return accountApi.getAccountDetail(accountNumber);
	}

	@Override
	public CreditCard getCreditCard(String cardId) {
		return creditCardApi.getCreditCardDetail(cardId);
	}

	@Override
	public DebitCard getDebitCard(String cardId) {
		return debitCardApi.getDebitCardDetail(cardId);
	}
}
