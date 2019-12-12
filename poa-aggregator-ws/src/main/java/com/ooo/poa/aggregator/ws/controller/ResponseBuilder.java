package com.ooo.poa.aggregator.ws.controller;

import com.ooo.poa.aggregator.ws.api.AppStatusAggr;

public class ResponseBuilder {

	public AppStatusAggr buildAppStatus(String message) {
		AppStatusAggr result = new AppStatusAggr();

		result.setMessage(message);

		return result;
	}
}
