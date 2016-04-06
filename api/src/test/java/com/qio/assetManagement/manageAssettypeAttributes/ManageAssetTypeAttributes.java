package com.qio.assetManagement.manageAssettypeAttributes;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.assetManagement.helper.AssetTypeTestHelper;
import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class ManageAssetTypeAttributes {

	private BaseHelper baseHelper = new BaseHelper();
	private MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private static String userName;;
	private static String password;;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;
	final static Logger logger = Logger.getLogger(ManageAssetTypeAttributes.class);

	private final int FIRST_ELEMENT = 0;
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		microservice = Microservice.ASSET_TYPE.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
	}
	
	@Before
	public void initSetupBeforeEveryTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-453 (AssetType with two Attributes with same abbreviation)
	@Test
	public void shouldNotCreateAssetTypeWhenTwoAttrsHaveSameAbbr() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		// Setting AssetType Attribute abbreviation of first attribute to the value of abbr of second attribute
		String abbrForSecondAttribute=requestAssetType.getAttributes().get(FIRST_ELEMENT + 1).getAbbreviation();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(abbrForSecondAttribute);
	
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not Contain Duplicate Entries",
				serverResp);
	}
	
	
	// RREHM-451 (AssetType Attribute abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		String defaultAbbr=requestAssetType.getAttributes().get(FIRST_ELEMENT).getAbbreviation();
		int count=AssetTypeTestHelper.SPECIAL_CHARS.length();
		
		for (int i=0; i < count; i++) {
			requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(AssetTypeTestHelper.SPECIAL_CHARS.charAt(i)+defaultAbbr);
					
			serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Attribute Abbreviation must not contain illegal characters",
				serverResp);
		}
	}
	
	// RREHM-446 (AssetType Attribute abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("This Abbreviation contains spaces");
			
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation must not contain Spaces",
				serverResp);
	}
		
	// RREHM-450 (AssetType Attribute abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("");
		
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not be Empty or Null",
				serverResp);
	}
	
	// RREHM-481 (AssetType Attribute abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		//Example
		//requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.String);
	
		// Setting AssetType Attribute abbreviation to null, so that it is not sent in the request.
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(null);
		
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-443 (AssetType Attribute abbreviation is longer than 50 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
		
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should Less Than 50 Character",
				serverResp);
	}
	
	// RREHM-456 (AssetType Attribute name is set to blank, i.e. name = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName("");
		
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute name should not be empty or null",
				serverResp);
	}
	
	// RREHM-479 (AssetType Attribute name is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		// Setting AssetType Attribute name to null, so that it is not sent in the request.
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName(null);
			
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-452 (AssetType Attribute name is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName("256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");
			
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Name should be less than 255 characters",
				serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
	@Ignore
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneAttrOfFloatDataType() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.Float);

		int expectedRespCode = 201;
		
		responseAssetType = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
		
		logger.info(responseAssetType.get_links().getSelf().getHref());
		logger.info(responseAssetType.getAttributes().get(0).get_links().getSelf().getHref());
		
		//assertEquals("Unexpected response code", requestAssetType, responseAssetType);
	}
}
