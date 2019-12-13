package com.ooo.poa.aggregator.ws.controller;

import org.springframework.http.HttpStatus;

public class AggregatorExceptionHandler {

	// TODO OOO handle more exceptions and error cases + add logging
	public HttpStatus toHttpStatus(Exception exception) {

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
