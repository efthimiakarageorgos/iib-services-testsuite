package com.qio.assetManagement.manageAssetTypeAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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
import com.qio.testHelper.TestHelper;


public class CreateAssetTypeAttributesTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;
	private static ArrayList<String> idsForAllCreatedAssetTypes;
	
	final static Logger logger = Logger.getLogger(CreateAssetTypeAttributesTest.class);
	private final int FIRST_ELEMENT = 0;
		
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests(Microservice.ASSET.toString());
		assetTypeAPI = new MAssetTypeAPIHelper();
		idsForAllCreatedAssetTypes = new ArrayList<String>();
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

		for (String assetTypeId : idsForAllCreatedAssetTypes) {
			TestHelper.deleteRequestObj(baseHelper, microservice, environment, assetTypeId, apiRequestHeaders,
					assetTypeAPI, AssetType.class);
		}
	}
	
	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-38))
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-843 ()
	// RREHM-842 ()
	// RREHM-841 ()
	// RREHM-847 ()
	// RREHM-849 ()
	// RREHM-848 ()
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
	// RREHM-840 ()
	// RREHM-846 ()
	// RREHM-844 ()
	// RREHM-838 ()
	
	
	
	/*
	 * POSITIVE TESTS END
	 */
}
