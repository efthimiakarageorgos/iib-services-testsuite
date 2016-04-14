package com.qio.assetManagement.manageAssets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;

//import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
//import com.qio.model.assetType.AssetType;
//import com.qio.model.assetType.helper.AssetTypeHelper;

import com.qio.lib.apiHelpers.MAssetAPIHelper;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.model.asset.Asset;
import com.qio.model.asset.helper.AssetHelper;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;

//import com.qio.lib.apiHelpers.MTenantAPIHelper;
//import com.qio.model.tenant.Tenant;
//import com.qio.model.tenant.helper.TenantHelper;

import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.log4j.Logger;

public class AssetsTest {
	private BaseHelper baseHelper = new BaseHelper();
//	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
//	private AssetTypeHelper assetTypeHelper;
//	private AssetType requestAssetType;
//	private AssetType responseAssetType;
//	
//	private  MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
//	private TenantHelper tenantHelper;
//	private Tenant requestTenant;
//	private Tenant responseTenant;
	
	private  MAssetAPIHelper assetAPI = new MAssetAPIHelper();
	private AssetHelper assetHelper;
	private Asset requestAsset;
	private Asset responseAsset;
	
	private static String userName;
	private static String password;
//	private static String microserviceAT;
//	private static String microserviceT;
	private static String microserviceAS;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	
	private ServerResponse serverResp;
	final static Logger logger = Logger.getRootLogger();

	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
//		microserviceAT = Microservice.ASSET_TYPE.toString();
//		microserviceT = Microservice.TENANT.toString();
		microserviceAS = Microservice.ASSET.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
	}
	
	@Before
	public void initSetupBeforeEveryTest()  {
		// Initializing a new set of objects before each test case.
//		assetTypeHelper = new AssetTypeHelper();
//		requestAssetType = new AssetType();
//		responseAssetType = new AssetType();
	
		serverResp = new ServerResponse();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-XXX (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
//		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
//		responseAssetType = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microserviceAT, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
//		String assetTypeHref=responseAssetType.get_links().getSelf().getHref();
//		
//		int i=assetTypeHref.lastIndexOf("/");
//		int length=assetTypeHref.length();
//		String assetTypeId=assetTypeHref.substring(i+1, length);
//		
//		logger.info("aaa "+assetTypeId);
//			
//		tenantHelper = new TenantHelper();
//		requestTenant = new Tenant();
//		responseTenant = new Tenant();
//		
//		requestTenant = tenantHelper.getTenant();
//		responseTenant = TestHelper.getResponseObjForCreate(baseHelper, requestTenant, microserviceT, environment, apiRequestHeaders, tenantAPI, Tenant.class);
//		
//		
//		String tenantId=responseTenant.getTenantId();
//		logger.info("aaa "+tenantId);
		
		
		requestAsset = assetHelper.getAsset();
			
		// Setting Asset abbreviation to contain spaces
		String abbr=requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space"+abbr);
			
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Abbreviation must not contain Spaces",
				serverResp);
	}
	
	// RREHM-XXX (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();
		
		// Setting Asset abbreviation to be longer than 50 chars
		requestAsset.setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
						
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset  abbreviation Should Less Than 50 Character",
				serverResp);
	}
	
	// RREHM-XXX (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();
		// Setting Asset abbreviation to null
		requestAsset.setAbbreviation("");
							
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset abbreviation Should not be Empty",
				serverResp);
	}
		
	// RREHM-XXX (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();				
		// Setting Asset abbreviation to null
		requestAsset.setAbbreviation(null);
								
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset abbreviation is a required field.",
				serverResp);
	}
	
	// RREHM-xxx (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();
		
		String defaultAbbr=requestAsset.getAbbreviation();
		int count = TestHelper.SPECIAL_CHARS.length();
			
		for (int i=0; i < count; i++) {
			requestAsset.setAbbreviation(TestHelper.SPECIAL_CHARS.charAt(i)+defaultAbbr);
						
			serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
			
			CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset abbreviation must not contain illegal characters", serverResp);
		}
	}
		
	// RREHM-xxx (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();				
		// Setting Asset name to blank
		requestAsset.setName("");
								
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset name Should not Empty",
				serverResp);
	}
		
	// RREHM-yyy (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		assetHelper = new AssetHelper("WithNoAttributesAndParameters");
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		requestAsset = assetHelper.getAsset();		
		// Setting Asset name to null
		requestAsset.setName(null);
						
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microserviceAS, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset name is a required field.",
				serverResp);
	}
	
		
//	// RREHM-ZZZ (Asset name is longer than 50 chars)
//	@Test
//	public void shouldNotCreateAssetTypeWhenNameIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
//				
//		// Setting AssetType name to be longer than 50 chars
//		requestAssetType.setName("51charlong51charlong51charlong51charlong51charSlong");
//				
//		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microserviceAT, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
//		
//		CustomAssertions.assertServerError(500,
//				"com.qiotec.application.exceptions.InvalidInputException",
//				"Asset Type Name should be less than 50 characters",
//				serverResp);
//	}
//	
//	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
