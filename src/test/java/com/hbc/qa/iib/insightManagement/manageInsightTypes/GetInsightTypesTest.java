/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.insightManagement.manageInsightTypes;

import org.junit.Before;
import org.junit.BeforeClass;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import com.hbc.qa.lib.ehm.apiHelpers.insights.MInsightTypeAPIHelper;
import com.hbc.qa.lib.ehm.model.insightType.InsightType;
import com.hbc.qa.lib.ehm.model.insightType.helper.InsightTypeHelper;
import com.hbc.qa.lib.exception.ServerResponse;


public class GetInsightTypesTest extends BaseTestSetupAndTearDown {

	private static MInsightTypeAPIHelper insightTypeAPI;
	private InsightTypeHelper insightTypeHelper;
	private InsightType requestInsightType;
	private InsightType responseInsightType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests("insight");
		insightTypeAPI = new MInsightTypeAPIHelper();
	}
	
	@Before
	public void initSetupBeforeEceryTest(){
		// Initializing a new set of objects before each test case.
		insightTypeHelper = new InsightTypeHelper();
		requestInsightType = new InsightType();
		responseInsightType = new InsightType();
		serverResp = new ServerResponse();
	}
	
	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1189")) and issue in  linkedIssues("RREHM-949") 
	
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
