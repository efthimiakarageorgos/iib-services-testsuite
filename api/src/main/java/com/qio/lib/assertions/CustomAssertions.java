package com.qio.lib.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.qio.lib.exception.ServerResponse;

/*
 * This class can be expanded with methods to add more generalized custom assertions.
 */
public class CustomAssertions {

	public static void assertServerError(int expectedRespCode, String expectedExceptionMsg, String expectedMsg, ServerResponse serverResp) {
		assertServerError(expectedRespCode, expectedExceptionMsg, serverResp);
		assertEquals(expectedMsg, serverResp.getMessage());
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
		assertRequestAndResponseObj(expectedRespCode, actualRespCode, null, null);
	}

}