package com.qio.assetManagement.manageAssettypes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.assetManagement.helper.AssetTypeTestHelper;
import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;


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
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	
	@Before
	public void initTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
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
			
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Abbreviation must not contain Spaces",
				serverResp);
	}
	
	// RREHM-436 (AssetType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
						
		// Setting AssetType abbreviation to be longer than 50 chars
		requestAssetType.setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
						
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Abbreviation Should Less Than 50 Character",
				serverResp);
	}
	
	// RREHM-468 (AssetType abbreviation is blank)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
							
		// Setting AssetType abbreviation to null
		requestAssetType.setAbbreviation("");
							
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Abbreviation Should not be Empty or Null",
				serverResp);
	}
		
	// RREHM-385 (AssetType abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
								
		// Setting AssetType abbreviation to null
		requestAssetType.setAbbreviation(null);
								
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-433 (AssetType abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
			
		String defaultAbbr=requestAssetType.getAbbreviation();
		int count = AssetTypeTestHelper.SPECIAL_CHARS.length();
			
		for (int i=0; i < count; i++) {
			requestAssetType.setAbbreviation(AssetTypeTestHelper.SPECIAL_CHARS.charAt(i)+defaultAbbr);
						
			serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
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
								
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Name Should not Empty or Null",
				serverResp);
	}
		
	// RREHM-384 (AssetType Name is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
						
		// Setting AssetType abbreviation to null
		requestAssetType.setName(null);
						
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
		
	// RREHM-437 (AssetType name is longer than 50 chars)
	// RREHM-1627 BUG - Uncomment after bug is fixed
	@Ignore
	public void shouldNotCreateAssetTypeWhenNameIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
				
		// Setting AssetType name to be longer than 50 chars
		requestAssetType.setName("51charlong51charlong51charlong51charlong51charSlong");
				
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Name Should Less Than 50 Character",
				serverResp);
	}
	
	
	// RREHM-440 (AssetType description is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenDescriptionIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
					
		// Setting AssetType description to be longer than 255 chars
		requestAssetType.setDescription("256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");
					
		serverResp = AssetTypeTestHelper.getAssetTypeCreateResponseObj(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Description should be less than 255 characters",
				serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
