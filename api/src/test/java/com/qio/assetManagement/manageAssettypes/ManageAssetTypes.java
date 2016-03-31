package com.qio.assetManagement.manageAssettypes;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
//import com.qio.lib.connection.ConnectionResponse;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.model.assetType.helper.ParameterDataType;


public class ManageAssetTypes {

	private BaseHelper baseHelper = new BaseHelper();
	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private String userName = "technician";
	private String password = "user@123";
	private String microservice = "asset-types";
	private String environment = ".qiotec.internal";
	private APIHeaders apiRequestHeaders = new APIHeaders(userName, password);
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;

	private final int DEFAULT_ELEMENT = 0;
	//JEET:
	//Could this be pulled out so it can be reused and if we need to change it, we change in one place?
	private String specialChars="~^%{&@}$#*()+=!~";
	
	@Before
	public void initTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-435 (AssetType abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		//requestAssetType = assetTypeHelper.getAssetTypeWithDefaultParameter();
			
		// Setting AssetType abbreviation to contain spaces
		String abbr=requestAssetType.getAbbreviation();
		requestAssetType.setAbbreviation("Abrr has a space"+abbr);
			
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
		//Call another class for custom validations; pass the response + request from the previous class call
		//It will grab the asset type abbr from the prev request and query assets types based on abbr - response
		//should be nothing in this specific test 
		//compare response of query with nothing
		//also compare the stuff you have below
			
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Abbreviation must not contain Spaces";
			
		CustomAssertions.assertServerError(expectedRespCode,
				expectedExceptionMsg,
				expectedMsg,
				serverResp);
	}
	
	// RREHM-436 (AssetType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
						
		// Setting AssetType abbreviation to be longer than 50 chars
		requestAssetType.setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
						
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
						
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Abbreviation Should Less Than 50 Character";
						
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
	
	// RREHM-468 (AssetType abbreviation is blank)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
							
		// Setting AssetType abbreviation to null
		requestAssetType.setAbbreviation("");
							
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
							
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Abbreviation Should not be Empty or Null";
							
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
		
	// RREHM-385 (AssetType abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
								
		// Setting AssetType abbreviation to null
		requestAssetType.setAbbreviation(null);
								
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
								
		int expectedRespCode = 500;
		String expectedExceptionMsg = "java.lang.NullPointerException";
		String expectedMsg = "No message available";
								
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
	
	// RREHM-433 (AssetType abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
			
		String defaultAbbr=requestAssetType.getAbbreviation();
		int count=specialChars.length();
			
		for (int i=0; i < count; i++) {
			requestAssetType.setAbbreviation(specialChars.charAt(i)+defaultAbbr);
						
			ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Abbreviation must not contain illegal characters", serverResp);
		}
	}
		
		
	// RREHM-384 (AssetType name is blank)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
								
		// Setting AssetType abbreviation to null
		requestAssetType.setName("");
								
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
								
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Name Should not Empty or Null";
								
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
		
	// RREHM-384 (AssetType Name is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
						
		// Setting AssetType abbreviation to null
		requestAssetType.setName(null);
						
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
						
		int expectedRespCode = 500;
		String expectedExceptionMsg = "java.lang.NullPointerException";
		String expectedMsg = "No message available";
						
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
	
		
	// RREHM-437 (AssetType name is longer than 50 chars)
	// RREHM-1627 BUG - Uncomment after bug is fixed
//	@Test
	public void shouldNotCreateAssetTypeWhenNameIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
				
		// Setting AssetType name to be longer than 50 chars
		requestAssetType.setName("51charlong51charlong51charlong51charlong51charSlong");
				
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
				
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Name Should Less Than 50 Character";
				
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
	
	
	// RREHM-440 (AssetType description is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenDescriptionIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
					
		// Setting AssetType description to be longer than 255 chars
		requestAssetType.setDescription("256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");
					
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
					
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Asset Type Description should be less than 255 characters";
					
		CustomAssertions.assertServerError(expectedRespCode, expectedExceptionMsg, expectedMsg, serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
