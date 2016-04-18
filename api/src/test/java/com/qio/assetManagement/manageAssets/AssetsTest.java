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

import com.qio.lib.apiHelpers.MAssetAPIHelper;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.model.asset.Asset;
import com.qio.model.asset.helper.AssetHelper;

import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;

import com.qio.testHelper.TestHelper;
import com.qio.testHelper.AssetTypeTestHelper;
import com.qio.testHelper.TenantTestHelper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.log4j.Logger;

public class AssetsTest {
	private BaseHelper baseHelper = new BaseHelper();
	private static AssetTypeTestHelper assetTypeTestHelper;
	private static TenantTestHelper tenantTestHelper;
	
	private  MAssetAPIHelper assetAPI = new MAssetAPIHelper();
	private AssetHelper assetHelper;
	private Asset requestAsset;
	private Asset responseAsset;
	
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	
	private static AssetType responseAssetTypePreDef;
	private static String assetTypeId;
	private static Tenant responseTenantPreDef;
	private static String tenantId;
	
	private ServerResponse serverResp;
	final static Logger logger = Logger.getRootLogger();
	
	@BeforeClass
	public static void initSetupBeforeAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		microservice = Microservice.ASSET.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
		
		assetTypeTestHelper = new AssetTypeTestHelper();
		responseAssetTypePreDef = new AssetType();
		responseAssetTypePreDef = assetTypeTestHelper.createAssetType("WithNoAttributesAndParameters");
		
		String[] assetTypeHrefLinkSplitArray = (responseAssetTypePreDef.get_links().getSelf().getHref()).split("/");
		assetTypeId = assetTypeHrefLinkSplitArray[assetTypeHrefLinkSplitArray.length - 1];
		
		tenantTestHelper = new TenantTestHelper();
		responseTenantPreDef = new Tenant();
		responseTenantPreDef = tenantTestHelper.createTenant();
		tenantId = responseTenantPreDef.getTenantId();
	}
	
	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// Initializing a new set of objects before each test case.	
		assetHelper = new AssetHelper();
		requestAsset = new Asset();
		responseAsset = new Asset();
		
		serverResp = new ServerResponse();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-629 (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		//requestAsset = assetHelper.getAssetCreateDependencies("WithNoAttributesAndParameters");
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		
		logger.info("assetTypeId "+assetTypeId + " tenantId " + tenantId);
		
		String abbr=requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space"+abbr);
		
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Abbreviation must not contain Spaces",
				serverResp);
	}
	
	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		
		requestAsset.setAbbreviation(TestHelper.FIFTYONE_CHARS);
						
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset  abbreviation Should Less Than 50 Character",
				serverResp);
	}
	
	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");
							
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset abbreviation Should not be Empty",
				serverResp);
	}
		
	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);				
		requestAsset.setAbbreviation(null);
								
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset abbreviation is a required field.",
				serverResp);
	}
	
//	// RREHM-632 (Asset abbreviation contains special chars)
//	@Test
//	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);	
//		
//		String defaultAbbr=requestAsset.getAbbreviation();
//		int count = TestHelper.SPECIAL_CHARS.length();
//			
//		for (int i=0; i < count; i++) {
//			requestAsset.setAbbreviation(TestHelper.SPECIAL_CHARS.charAt(i)+defaultAbbr);
//						
//			serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
//			
//			CustomAssertions.assertServerError(500,
//				"com.qiotec.application.exceptions.InvalidInputException",
//				"Asset abbreviation must not contain illegal characters", serverResp);
//		}
//	}
//		
	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);			
		requestAsset.setName("");
								
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset name Should not Empty",
				serverResp);
	}
		
	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);		
		requestAsset.setName(null);
						
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset name is a required field.",
				serverResp);
	}
	
		
	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);		
				
		requestAsset.setName(TestHelper.TWOFIFTYSIX_CHARS);
				
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAsset, microservice, environment, apiRequestHeaders, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset name should be less than 255 characters",
				serverResp);
	}
	
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
