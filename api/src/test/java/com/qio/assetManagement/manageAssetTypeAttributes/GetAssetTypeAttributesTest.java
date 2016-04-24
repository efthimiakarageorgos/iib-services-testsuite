package com.qio.assetManagement.manageAssetTypeAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;


public class GetAssetTypeAttributesTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;
	
	final static Logger logger = Logger.getLogger(GetAssetTypeAttributesTest.class);
	private final int FIRST_ELEMENT = 0;
		
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests(Microservice.ASSET.toString());
		assetTypeAPI = new MAssetTypeAPIHelper();
	}
	
	@Before
	public void initSetupBeforeEveryTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}
	
	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		baseCleanUpAfterAllTests(assetTypeAPI);
	}
	
	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-950), linkedIssues(RREHM-951))
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1268 ()
	// RREHM-1253 ()
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
	// RREHM-1267 ()
	// RREHM-1251 ()
	// RREHM-1256 ()
	
	
	
	
	/*
	 * POSITIVE TESTS END
	 */
}
