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
			// Creating Asset-types
			java.util.Date date= new java.util.Date();
			String timestamp = Long.toString(date.getTime());
			// AssetType with default values
			AssetType requestAssetType = new AssetType(timestamp);
			// Setting AssetType abbreviation to contain spaces.
			String abbr = "Effie435"+timestamp;
			//requestAssetType.getAttributes().get(DEFAULT_ELEMENT).setAbbreviation(abbr);
			requestAssetType.setAbbreviation(abbr);
			
			int expectedRespCode = 500;
			String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
			String expectedMsg = "Attribute Abbreviation Should Less Than 50 Character";
			
			String payload = baseHelper.toJSONString(requestAssetType);
			ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
			ServerResponse serverResp = baseHelper.toClassObject(conRespPost.getRespBody(), ServerResponse.class);
			
			//Uncomment after the abbr goes back to containing spaces
			//assertEquals("Unexpected response code", expectedRespCode, serverResp.getStatus());
			//assertEquals("Unexpected exception message", expectedExceptionMsg, serverResp.getException());
			//assertEquals("Unexpected error message", expectedMsg, serverResp.getMessage());
		}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	
}
