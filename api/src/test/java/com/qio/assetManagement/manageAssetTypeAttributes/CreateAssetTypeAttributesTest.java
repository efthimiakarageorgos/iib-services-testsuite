package com.qio.assetManagement.manageAssetTypeAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class CreateAssetTypeAttributesTest {	
	private static BaseHelper baseHelper;
	private static MAssetTypeAPIHelper assetTypeAPI;
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;
	private static ArrayList<String> idsForAllCreatedAssetTypes;
	
	final static Logger logger = Logger.getLogger(CreateAssetTypeAttributesTest.class);
	private final int FIRST_ELEMENT = 0;
		
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		microservice = Microservice.ASSET.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
		
		baseHelper = new BaseHelper();
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
