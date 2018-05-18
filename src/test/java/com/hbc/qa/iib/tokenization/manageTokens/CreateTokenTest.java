/**
 * © HBC Shared Services QA 2018. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF HBC.
 */

package com.hbc.qa.iib.tokenization.manageTokens;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

public class CreateTokenTest extends BaseTestSetupAndTearDown {

	private static MTokenRequestAPIHelper tokenRequestAPIHelper;
	private TokenHelper tokenHelper;
	private TokenRequest tokenRequest;
	private TokenResponse tokenResponse;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("tokenization");
		tokenRequestAPIHelper = new MTokenRequestAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		// Initializing a new set of objects before each test case.
		tokenHelper = new TokenHelper();
		tokenRequest = tokenHelper.getTokenRequest();
		tokenResponse = new TokenResponse();
		serverResp = new ServerResponse();
	}

	//This will not work as Delete is not allowed!
	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(tokenRequestAPIHelper);
	}
	

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1235")) and issue in linkedIssues("RREHM-XXX")
	// None of the test are marked as automated as they need to be cleaned up

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-516 (InsightType has non unique abbreviation)
	@Test
	public void shouldNotCreateTokenWhenXXX() {
		tokenRequest = tokenHelper.getTokenRequest();
		tokenRequest.setBanner("LTS");

		//serverResp = MAbstractAPIHelper.getResponseObjForCreate(tokenRequest, microservice, environment, apiRequestHelper, tokenRequestAPIHelper, ServerResponse.class);
        //CustomAssertions.assertServerError(400, null, "Invalid Banner", serverResp);
		tokenResponse = MAbstractAPIHelper.getResponseObjForCreate(tokenRequest, microservice, environment, apiRequestHelper, tokenRequestAPIHelper, TokenResponse.class);

		CustomAssertions.assertResponseCode(400, MAbstractAPIHelper.responseCodeForInputRequest);
        CustomAssertions.assertEqualityCheckOnInputFields("Invalid Banner", tokenResponse.getResponseMessage());
		CustomAssertions.assertEqualityCheckOnInputFields("1", tokenResponse.getResponseCode());
		CustomAssertions.assertEqualityCheckOnInputFields(tokenRequest.getBanner(), tokenResponse.getBanner());
		CustomAssertions.assertEqualityCheckOnInputFields(tokenRequest.getCardNumber(), tokenResponse.getCardNumber());
		CustomAssertions.assertEqualityCheckOnInputFields(null, tokenResponse.getToken());
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
