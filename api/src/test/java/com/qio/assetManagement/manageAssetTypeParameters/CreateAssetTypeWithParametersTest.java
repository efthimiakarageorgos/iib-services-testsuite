
package com.qio.assetManagement.manageAssetTypeParameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
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


public class CreateAssetTypeWithParametersTest  {

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
		// Setting AssetType Parameter abbreviation to be longer than 255 chars.
		String abbrLongerThan255Chars = "256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong";
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(abbrLongerThan255Chars);
		
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

		// RV1: comparing CreatedObject with CreateRequest, along with response
		// codes.
		CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAssetType,
				responseAssetType);

		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedAssetTypes.add(assetTypeId);

		AssetType committedAssetType = TestHelper.getResponseObjForRetrieve(baseHelper, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, AssetType.class);

		// RV2: comparing CommittedObject with CreatedObject, without the
		// response codes.
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-611 ()
	
	// RREHM-633 ()
		
	// RREHM-1077 ()
		
	// RREHM-1614 ()
	
	/*
	 * POSITIVE TESTS END
	 */

}