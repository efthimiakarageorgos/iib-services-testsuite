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
			//requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
			
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
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
