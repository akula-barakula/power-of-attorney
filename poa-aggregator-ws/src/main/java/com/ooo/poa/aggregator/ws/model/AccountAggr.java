package com.ooo.poa.aggregator.ws.model;

public class AccountAggr {

	private String number;
	private String iban;
	private String owner;

	private Integer balance;
	private String created;
	private String ended;


	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIban() {
		return iban;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreated() {
		return created;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}

	public String getEnded() {
		return ended;
	}
}
