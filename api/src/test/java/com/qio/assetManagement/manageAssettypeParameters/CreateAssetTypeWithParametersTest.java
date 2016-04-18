package com.qio.assetManagement.manageAssettypeParameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class CreateAssetTypeWithParametersTest {

	private BaseHelper baseHelper = new BaseHelper();
	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

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
	}
	
	@Before
	public void initSetupBeforeEveryTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}
	
	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in  linkedIssues("RREHM-41")
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1626 (AssetType Parameter abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to blank
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");
	
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not be Empty or Null",
				serverResp);
	}
	
	// RREHM-1626 (AssetType Parameter abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to null, so that it is not sent in the request.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);
		
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-1112 (AssetType Parameter abbreviation is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(TestHelper.TWOFIFTYSIX_CHARS);
		
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should Less Than 255 Character",
				serverResp);
	}
	
	// RREHM-1078 (AssetType Parameter abbreviation has spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrHasSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting AssetType Parameter abbreviation to have spaces.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("Abrr has a space");
			
		serverResp = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation must not contain Spaces",
				serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterOfFloatDataType() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.Float);

		responseAssetType = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment,
				apiRequestHeaders, assetTypeAPI, AssetType.class);
		final Logger logger = Logger.getRootLogger();
		logger.info(responseAssetType.get_links().getSelf().getHref());
		
		String[] assetTypeHrefLinkSplitArray = (responseAssetType.get_links().getSelf().getHref()).split("/");
		String assetTypeId = assetTypeHrefLinkSplitArray[assetTypeHrefLinkSplitArray.length - 1];
		
		AssetType responseAssetTypeFromGetRequestOnID = baseHelper.toClassObject((assetTypeAPI.retrieve(microservice,
				environment, apiRequestHeaders, assetTypeId).getRespBody()), AssetType.class);
		
		CustomAssertions.assertRequestAndResponseObj(201, TestHelper.actualResponseCode, requestAssetType,
				responseAssetTypeFromGetRequestOnID);

		// TODO: This needs to be generalized, as we might need to call it after
		// every test method.
		// Consider recording all assetTypeId in arraylist and then delete them
		// all in the @After method.
		assetTypeAPI.delete(microservice, environment, apiRequestHeaders, assetTypeId);
	}

	// RREHM-611 ()
	
	// RREHM-633 ()
	
	// RREHM-1077 ()
	
	// RREHM-1614 ()
	
	/*
	 * POSITIVE TESTS END
	 */

}
