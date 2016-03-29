package com.qio.lib.assertions;

import static org.junit.Assert.assertEquals;

import com.qio.lib.exception.ServerResponse;
/*
 * This class can be expanded with methods to add more generalized custom assertions.
 */
public class CustomAssertions {
	
	public static void assertServerError(int expectedRespCode, String expectedExceptionMsg, String expectedMsg, ServerResponse serverResp){
		assertEquals(expectedRespCode, serverResp.getStatus());
		assertEquals(expectedExceptionMsg, serverResp.getException());
		assertEquals(expectedMsg, serverResp.getMessage());
	}
}
