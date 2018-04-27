/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.insightManagement.manageInsightActivityTypes;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import org.junit.Before;
import org.junit.BeforeClass;

import com.hbc.qa.lib.ehm.apiHelpers.insights.MActivityTypeAPIHelper;
import com.hbc.qa.lib.ehm.model.activityType.ActivityType;
import com.hbc.qa.lib.ehm.model.activityType.helper.ActivityTypeHelper;
import com.hbc.qa.lib.exception.ServerResponse;

public class GetActivityTypesTest extends BaseTestSetupAndTearDown {

	private static MActivityTypeAPIHelper activityTypeAPI;
	private ActivityTypeHelper activityTypeHelper;
	private ActivityType requestActivityType;
	private ActivityType responseActivityType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
        activityTypeAPI = new MActivityTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		// Initializing a new set of objects before each test case.
        activityTypeHelper = new ActivityTypeHelper();
        requestActivityType = new ActivityType();
        responseActivityType = new ActivityType();
		serverResp = new ServerResponse();
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-XXX")) and issue in linkedIssues("RREHM-XXX")

	/*
	 * NEGATIVE TESTS START
	 */
	// RREHM-1248 ()

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1247 ()

	// RREHM-1245 ()
}
