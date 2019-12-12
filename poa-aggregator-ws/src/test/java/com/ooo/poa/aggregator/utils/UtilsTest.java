package com.ooo.poa.aggregator.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void toAccountNumber() {
		assertEquals("123456789", Utils.toAccountNumber("NL23RABO123456789"));
	}
}
