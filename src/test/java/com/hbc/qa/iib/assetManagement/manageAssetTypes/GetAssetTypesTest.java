/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.assetManagement.manageAssetTypes;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.hbc.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.hbc.qa.lib.ehm.model.assetType.AssetType;
import com.hbc.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import com.hbc.qa.lib.exception.ServerResponse;


public class GetAssetTypesTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}
	
	@Before
	public void initSetupBeforeEceryTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}
	
	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}
	
	// Matching test cases in Test Case Management (Jira/Zephyr):
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
