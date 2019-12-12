package com.ooo.poa.aggregator.service;

import java.util.List;

import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PowerOfAttorney;
import com.ooo.poa.client.model.PowerOfAttorneyReference;

public interface PoaClient {

	List<PowerOfAttorneyReference> getAllPowerOfAttorneyReferences();

	PowerOfAttorney getPowerOfAttorney(String poaId);

	Account getAccount(String accountNumber);

	CreditCard getCreditCard(String cardId);

	DebitCard getDebitCard(String cardId);
}
