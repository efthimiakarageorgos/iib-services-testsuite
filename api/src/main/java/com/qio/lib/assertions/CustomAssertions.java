package com.qio.lib.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.qio.lib.exception.ServerResponse;

/*
 * This class can be expanded with methods to add more generalized custom assertions.
 */
public class CustomAssertions {
	
	private static String DATE_FORMAT_REGEX = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])T(00|0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])Z$";

	public static void assertServerError(int expectedRespCode, String expectedExceptionMsg, String expectedMsg, ServerResponse serverResp) {
		assertServerError(expectedRespCode, expectedExceptionMsg, serverResp);
		assertTrue(serverResp.getMessage().equals(expectedMsg) || serverResp.getMessage().contains(expectedMsg));
	}

	public static void assertServerError(int expectedRespCode, String expectedExceptionMsg, ServerResponse serverResp) {
		assertEquals(expectedRespCode, serverResp.getStatus());
		assertEquals(expectedExceptionMsg, serverResp.getException());
	}

	public static void assertRequestAndResponseObj(int expectedRespCode, int actualRespCode, Object requestObj, Object responseObj) {
		assertEquals(expectedRespCode, actualRespCode);
		assertTrue(requestObj.equals(responseObj));
	}

	public static void assertRequestAndResponseObj(Object requestObj, Object responseObj) {
		// Here the expected and actual response codes do not matter, therefore
		// setting them both to 0, so as to ignore the assertion on them in the
		// called method.
		assertRequestAndResponseObj(0, 0, requestObj, responseObj);
	}

	public static void assertResponseCode(int expectedRespCode, int actualRespCode) {
		assertEquals(expectedRespCode, actualRespCode);
	}

	public static void assertRequestAndResponseObjForNullEqualityCheck(Object requestObj, Object responseObj) {
		assertEquals(requestObj, responseObj);
	}
	
	public static void assertDateFormat(String inputDate){
		assertDateFormat(inputDate, DATE_FORMAT_REGEX);
	}
	
	public static void assertDateFormat(String inputDate, String dateFormatRegex){
		assertTrue("Incorrect date format", inputDate.matches(dateFormatRegex));
	}

}