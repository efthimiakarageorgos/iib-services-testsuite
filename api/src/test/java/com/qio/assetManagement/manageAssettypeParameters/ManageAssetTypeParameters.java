package com.qio.assetManagement.manageAssettypeParameters;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.ParameterDataType;


public class ManageAssetTypeParameters {

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
	

	private final int FIRST_ELEMENT = 0;
	
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

	// RREHM-??? (AssetType Parameter abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to blank
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");
	
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not be Empty or Null",
				serverResp);
	}
	
	// RREHM-??? (AssetType Parameter abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to null, so that it is not sent in the request.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-1112 (AssetType Parameter abbreviation is longer than 255 Chars) WIP
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting AssetType Parameter abbreviation to be longer than 255 chars.
		String abbrLongerThan255Chars = "256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong";
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(abbrLongerThan255Chars);
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should Less Than 255 Character",
				serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
//	@Test
//	public void shouldCreateAssetTypeWithUniqueAbbrWithOneAttrOfFloatDataType() throws JsonGenerationException, JsonMappingException, IOException{
//		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.Float);
//
//		int expectedRespCode = 201;
//		
//		responseAssetType = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
//		
//		System.out.println(responseAssetType.get_links().getSelf().getHref());
//		System.out.println(responseAssetType.getAttributes().get(0).get_links().getSelf().getHref());
//		
		//Uncomment after figuring out why they are different
		//assertEquals("Unexpected response code", requestAssetType, responseAssetType);
//	}
}
