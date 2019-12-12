package com.ooo.poa.aggregator.utils;

public final class Utils {

	private Utils() {}


	/**
	 * https://bank.codes/iban/structure/netherlands/
	 */
	public static String toAccountNumber(String accountIban) {

		return accountIban.substring(8);
	}
}
