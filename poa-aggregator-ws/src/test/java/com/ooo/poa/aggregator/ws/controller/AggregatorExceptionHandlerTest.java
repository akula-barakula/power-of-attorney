package com.ooo.poa.aggregator.ws.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertSame;

public class AggregatorExceptionHandlerTest {

	private AggregatorExceptionHandler exceptionHandler;


	@Before
	public void before() {
		exceptionHandler = new AggregatorExceptionHandler();
	}


	@Test
	public void toHttpStatus() {

		assertSame(HttpStatus.INTERNAL_SERVER_ERROR, exceptionHandler.toHttpStatus(new Exception()));
	}
}
