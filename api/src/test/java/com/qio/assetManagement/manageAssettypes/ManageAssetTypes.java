package com.qio.assetManagement.manageAssettypes;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.AssetTypeAttributeDatatype;


public class ManageAssetTypes {

	BaseHelper baseHelper = new BaseHelper();
	MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	String userName = "technician";
	String password = "user@123";
	String microservice = "asset-types";
	String environment = ".qiotec.internal";
	APIHeaders apiRequestHeaders = new APIHeaders(userName, password);

	private final int DEFAULT_ELEMENT = 0;
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-435 (AssetType abbreviation contains spaces)
		@Test
		public void shouldNotCreateAssetTypeWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException{
			//Call a class that
			//a. Make a call to form the appropriate for the test case asset type message
			//b. Submit the request 
			//c. returns the response of the request + request body
			
			
			// Creating Asset-types
			java.util.Date date= new java.util.Date();
			String timestamp = Long.toString(date.getTime());
			// AssetType with default values
			AssetType requestAssetType = new AssetType(timestamp);
			// Setting AssetType abbreviation to contain spaces.
			String abbr = "Abrr has a space"+timestamp;
			//Note: this was replacing the abbreviation at the attribute level - added the right one
			//requestAssetType.getAttributes().get(DEFAULT_ELEMENT).setAbbreviation(abbr);
			requestAssetType.setAbbreviation(abbr);
			
			
			String payload = baseHelper.toJSONString(requestAssetType);
			ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
			ServerResponse serverResp = baseHelper.toClassObject(conRespPost.getRespBody(), ServerResponse.class);
			
			//Call another class for custom validations; pass the response + request from the previous class call
			//It will grab the asset type abbr from the prev request and query assets types based on abbr - response
			//should be nothing in this specific test 
			//compare response of query with nothing
			//also compare the stuff you have below
			
			int expectedRespCode = 500;
			String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
			String expectedMsg = "Asset Type Abbreviation must not contain Spaces";
			
			//Ignore comment -- Uncomment after the abbr goes back to containing spaces
			assertEquals("Unexpected response code", expectedRespCode, serverResp.getStatus());
			assertEquals("Unexpected exception message", expectedExceptionMsg, serverResp.getException());
			assertEquals("Unexpected error message", expectedMsg, serverResp.getMessage());
		}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
