package com.ooo.poa.aggregator.ws.model;

public class DebitCardAggr {

	private String id;
	private Integer cardNumber;
	private Integer sequenceNumber;
	private String cardHolder;

	private CardStatusAggr status;
	private LimitAggr atmLimit;
	private LimitAggr posLimit;
	private Boolean contactless;


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

    public void setStatus(CardStatusAggr status) {
        this.status = status;
    }

    public CardStatusAggr getStatus() {
        return status;
    }

	public void setAtmLimit(LimitAggr atmLimit) {
		this.atmLimit = atmLimit;
	}

	public LimitAggr getAtmLimit() {
		return atmLimit;
	}

	public void setPosLimit(LimitAggr posLimit) {
		this.posLimit = posLimit;
	}

	public LimitAggr getPosLimit() {
		return posLimit;
	}

	public void setContactless(Boolean contactless) {
		this.contactless = contactless;
	}

	public Boolean isContactless() {
		return contactless;
	}
}
