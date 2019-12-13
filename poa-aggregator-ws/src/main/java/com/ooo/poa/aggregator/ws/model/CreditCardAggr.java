package com.ooo.poa.aggregator.ws.model;

public class CreditCardAggr {

	private String id;
	private Integer cardNumber;
	private Integer sequenceNumber;
	private String cardHolder;

	private StatusAggr status;
	private Integer monhtlyLimit;


	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setCardNumber(Integer cardNumber) {
	    this.cardNumber = cardNumber;
	}

	public Integer getCardNumber() {
	    return cardNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
	    this.sequenceNumber = sequenceNumber;
	}

	public Integer getSequenceNumber() {
	    return sequenceNumber;
	}

	public void setCardHolder(String cardHolder) {
	    this.cardHolder = cardHolder;
	}

	public String getCardHolder() {
	    return cardHolder;
	}

	public void setStatus(StatusAggr status) {
		this.status = status;
	}

	public StatusAggr getStatus() {
		return status;
	}

	public void setMonhtlyLimit(Integer monhtlyLimit) {
		this.monhtlyLimit = monhtlyLimit;
	}

	public Integer getMonhtlyLimit() {
		return monhtlyLimit;
	}
}
