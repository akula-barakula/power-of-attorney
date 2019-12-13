package com.ooo.poa.aggregator.service.impl;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.ooo.poa.aggregator.AggregatorException;
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
		return call(() -> powerOfAttorneyApi.getAllPowerOfAttorneyReferencesWithHttpInfo());
	}

	@Override
	public PowerOfAttorney getPowerOfAttorney(String poaId) {
	    return call(() -> powerOfAttorneyApi.getPowerOfAttorneyDetailWithHttpInfo(poaId));
	}

	@Override
	public Account getAccount(String accountNumber) {
	    return call(() -> accountApi.getAccountDetailWithHttpInfo(accountNumber));
	}

	@Override
	public CreditCard getCreditCard(String cardId) {
		return call(() -> creditCardApi.getCreditCardDetailWithHttpInfo(cardId));
	}

	@Override
	public DebitCard getDebitCard(String cardId) {
		return call(() -> debitCardApi.getDebitCardDetailWithHttpInfo(cardId));
	}

	private <T> T call(Supplier<ResponseEntity<T>> supplier) {

	    try {
	        ResponseEntity<T> responseEntity = supplier.get();

	        return responseEntity.getBody();

	    } catch (HttpClientErrorException exception) {
	        throw new AggregatorException(exception);
	    }
	}
}
