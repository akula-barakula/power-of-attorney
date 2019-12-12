package com.ooo.poa.aggregator.ws.api;

import javax.validation.constraints.NotNull;

public class AppStatusAggr {

	@NotNull
	private String message;


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
