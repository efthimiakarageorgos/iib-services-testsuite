/**
 * © HBC Shared Services QA 2018. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF HBC.
 */

package com.hbc.qa.iib.tokenization.manageTokens;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TestWatcher;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import com.hbc.qa.lib.iib.apiHelpers.token.MTokenRequestAPIHelper;
import com.hbc.qa.lib.iib.model.token.TokenRequest;
import com.hbc.qa.lib.iib.model.token.TokenResponse;
import com.hbc.qa.lib.iib.model.token.helper.TokenHelper;
import com.hbc.qa.lib.iib.common.APITestUtil;
import com.hbc.qa.lib.assertions.CustomAssertions;
import com.hbc.qa.lib.exception.ServerResponse;
import com.hbc.qa.lib.common.MAbstractAPIHelper;
import com.hbc.qa.lib.common.BaseHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateTokenTest extends BaseTestSetupAndTearDown {

	private static MTokenRequestAPIHelper tokenRequestAPIHelper;
	private TokenHelper tokenHelper;
	private TokenRequest tokenRequest;
	private TokenResponse tokenResponse;
	private TokenResponse tokenResponseForComparison;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("tokenization");
        logger.info("    CHRISTOS");
		tokenRequestAPIHelper = new MTokenRequestAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		tokenHelper = new TokenHelper();
		tokenRequest = tokenHelper.getTokenRequest();
		tokenResponse = new TokenResponse();
		tokenResponseForComparison = new TokenResponse();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		//baseCleanUpAfterAllTests(tokenRequestAPIHelper);
	}

	@After
	public void tearDownEveryTest() {
		logger.info("    AFTER");
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1235")) and issue in linkedIssues("RREHM-XXX")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-516 (InsightType has non unique abbreviation)
	@Test
	public void shouldNotCreateTokenWhenXXX() {
		tokenRequest.setBanner("LT");

		tokenResponse = MAbstractAPIHelper.getResponseObjForCreate(tokenRequest, microservice, environment, apiRequestHelper, tokenRequestAPIHelper, TokenResponse.class);

		tokenResponseForComparison=tokenResponse;
		tokenResponseForComparison.setToken("1236512929");
        tokenResponseForComparison.setResponseCode("0");
        tokenResponseForComparison.setResponseMessage("success");

        CustomAssertions.assertRequestAndResponseObj(200, MAbstractAPIHelper.responseCodeForInputRequest, tokenResponse, tokenResponseForComparison);

		assertTrue(tokenResponseForComparison.equals(tokenResponse));
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-522 ()
	@Test
	public void shouldCreateInsightTypeWhenNameContainsSpecialChars() {
		tokenRequest = tokenHelper.getTokenRequest();
		tokenResponse = MAbstractAPIHelper.getResponseObjForCreate(tokenRequest, microservice, environment, apiRequestHelper, tokenRequestAPIHelper, TokenResponse.class);

		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, tokenResponse, tokenResponse);
	}
}
