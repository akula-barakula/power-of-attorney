package com.ooo.poa.aggregator.ws.api;

import java.util.List;

public class PowerOfAttorneyAggr {

	private String id;
	private String grantor;
	private String grantee;
	private AccountAggr account;
	private DirectionAggr direction;
	private List<AuthorizationAggr> authorizations;
	private List<CreditCardAggr> creditCards;
	private List<DebitCardAggr> debitCards;


	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setGrantor(String grantor) {
		this.grantor = grantor;
	}

	public String getGrantor() {
		return grantor;
	}

	public void setGrantee(String grantee) {
		this.grantee = grantee;
	}

	public String getGrantee() {
		return grantee;
	}

	public void setAccount(AccountAggr account) {
		this.account = account;
	}

	public AccountAggr getAccount() {
		return account;
	}

	public void setDirection(DirectionAggr direction) {
		this.direction = direction;
	}

	public DirectionAggr getDirection() {
		return direction;
	}

	public void setAuthorizations(List<AuthorizationAggr> authorizations) {
		this.authorizations = authorizations;
	}

	public List<AuthorizationAggr> getAuthorizations() {
		return authorizations;
	}

	public void setCreditCards(List<CreditCardAggr> creditCards) {
		this.creditCards = creditCards;
	}

	public List<CreditCardAggr> getCreditCards() {
		return creditCards;
	}

	public void setDebitCards(List<DebitCardAggr> debitCards) {
		this.debitCards = debitCards;
	}

	public List<DebitCardAggr> getDebitCards() {
		return debitCards;
	}
}
