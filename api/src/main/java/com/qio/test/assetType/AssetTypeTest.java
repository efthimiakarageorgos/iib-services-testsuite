package com.qio.test.assetType;

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


public class AssetTypeTest {

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

	// RREHM-481 (AssetType Attribute abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		// Creating data-types
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeInt = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Integer"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeDate = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Date"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeBool = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Boolean"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeFloat = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Float"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypePreList = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Option1"));
			add(new AssetTypeAttributeDatatype("Option2"));
		}};

		// Creating attributes
		List<AssetTypeAttribute> assetTypeAttrAll = new ArrayList<AssetTypeAttribute>();
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRInteger", "ABBRInteger Name", "ABBRInteger Desc", "ABBRInteger Unit", assetTypeAttrDatatypeInt));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRDate", "ABBRDate Name", "ABBRDate Desc", "", assetTypeAttrDatatypeDate));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRBoolean", "ABBRBoolean Name", "ABBRBoolean Desc", "", assetTypeAttrDatatypeBool));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRFloat", "ABBRFloat Name", "ABBRFloat Desc", "", assetTypeAttrDatatypeFloat));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRPredefinedList", "ABBRPredefinedList Name", "ABBRPredefinedList Desc", "", assetTypeAttrDatatypePreList));
		
		// Creating Asset-types
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		// AssetType with default values
		AssetType requestAssetType = new AssetType(timestamp);
		// Setting AssetType abbreviation to blank
		requestAssetType.getAttributes().get(DEFAULT_ELEMENT).setAbbreviation("");
		assetTypeAttrAll.addAll(requestAssetType.getAttributes());
		requestAssetType.setAttributes(assetTypeAttrAll);
		
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Attribute Abbreviation Should not be Empty or Null";
		
		String payload = baseHelper.toJSONString(requestAssetType);
		ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
		ServerResponse serverResp = baseHelper.toClassObject(conRespPost.getRespBody(), ServerResponse.class);
		
		assertEquals("Unexpected response code", expectedRespCode, serverResp.getStatus());
		assertEquals("Unexpected exception message", expectedExceptionMsg, serverResp.getException());
		assertEquals("Unexpected error message", expectedMsg, serverResp.getMessage());
	}
	
	// RREHM-481 (AssetType Attribute abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		// Creating data-types
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeInt = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Integer"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeDate = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Date"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeBool = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Boolean"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeFloat = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Float"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypePreList = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Option1"));
			add(new AssetTypeAttributeDatatype("Option2"));
		}};

		// Creating attributes
		List<AssetTypeAttribute> assetTypeAttrAll = new ArrayList<AssetTypeAttribute>();
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRInteger", "ABBRInteger Name", "ABBRInteger Desc", "ABBRInteger Unit", assetTypeAttrDatatypeInt));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRDate", "ABBRDate Name", "ABBRDate Desc", "", assetTypeAttrDatatypeDate));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRBoolean", "ABBRBoolean Name", "ABBRBoolean Desc", "", assetTypeAttrDatatypeBool));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRFloat", "ABBRFloat Name", "ABBRFloat Desc", "", assetTypeAttrDatatypeFloat));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRPredefinedList", "ABBRPredefinedList Name", "ABBRPredefinedList Desc", "", assetTypeAttrDatatypePreList));
		
		// Creating Asset-types
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		// AssetType with default values
		AssetType requestAssetType = new AssetType(timestamp);
		// Setting AssetType abbreviation to null, so that it is not sent in the request.
		requestAssetType.getAttributes().get(DEFAULT_ELEMENT).setAbbreviation(null);
		assetTypeAttrAll.addAll(requestAssetType.getAttributes());
		requestAssetType.setAttributes(assetTypeAttrAll);
		
		int expectedRespCode = 500;
		String expectedExceptionMsg = "java.lang.NullPointerException";
		String expectedMsg = "No message available";
		
		String payload = baseHelper.toJSONString(requestAssetType);
		ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
		ServerResponse serverResp = baseHelper.toClassObject(conRespPost.getRespBody(), ServerResponse.class);
		
		assertEquals("Unexpected response code", expectedRespCode, serverResp.getStatus());
		assertEquals("Unexpected exception message", expectedExceptionMsg, serverResp.getException());
		assertEquals("Unexpected error message", expectedMsg, serverResp.getMessage());
	}
	
	// RREHM-443 (AssetType Attribute abbreviation is longer than 50 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		// Creating data-types
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeInt = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Integer"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeDate = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Date"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeBool = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Boolean"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeFloat = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Float"));
		}};
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypePreList = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Option1"));
			add(new AssetTypeAttributeDatatype("Option2"));
		}};

		// Creating attributes
		List<AssetTypeAttribute> assetTypeAttrAll = new ArrayList<AssetTypeAttribute>();
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRInteger", "ABBRInteger Name", "ABBRInteger Desc", "ABBRInteger Unit", assetTypeAttrDatatypeInt));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRDate", "ABBRDate Name", "ABBRDate Desc", "", assetTypeAttrDatatypeDate));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRBoolean", "ABBRBoolean Name", "ABBRBoolean Desc", "", assetTypeAttrDatatypeBool));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRFloat", "ABBRFloat Name", "ABBRFloat Desc", "", assetTypeAttrDatatypeFloat));
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRPredefinedList", "ABBRPredefinedList Name", "ABBRPredefinedList Desc", "", assetTypeAttrDatatypePreList));
		
		// Creating Asset-types
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		// AssetType with default values
		AssetType requestAssetType = new AssetType(timestamp);
		// Setting AssetType abbreviation to be longer than 50 chars.
		String abbrLongerThan50Chars = "51charlong51charlong51charlong51charlong51charSlong";
		requestAssetType.getAttributes().get(DEFAULT_ELEMENT).setAbbreviation(abbrLongerThan50Chars);
		assetTypeAttrAll.addAll(requestAssetType.getAttributes());
		requestAssetType.setAttributes(assetTypeAttrAll);
		
		int expectedRespCode = 500;
		String expectedExceptionMsg = "com.qiotec.application.exceptions.InvalidInputException";
		String expectedMsg = "Attribute Abbreviation Should Less Than 50 Character";
		
		String payload = baseHelper.toJSONString(requestAssetType);
		ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
		ServerResponse serverResp = baseHelper.toClassObject(conRespPost.getRespBody(), ServerResponse.class);
		
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
	// RREHM-443 (AssetType Attribute abbreviation is longer than 50 Chars)
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneAttrOfFloatDataType() throws JsonGenerationException, JsonMappingException, IOException{
		// Creating data-types
		List<AssetTypeAttributeDatatype> assetTypeAttrDatatypeFloat = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype("Float"));
		}};

		// Creating attributes
		List<AssetTypeAttribute> assetTypeAttrAll = new ArrayList<AssetTypeAttribute>();
		assetTypeAttrAll.add(new AssetTypeAttribute("ABBRFloat", "ABBRFloat Name", "ABBRFloat Desc", "", assetTypeAttrDatatypeFloat));
		
		// Creating Asset-types
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		// AssetType with default values
		AssetType requestAssetType = new AssetType(timestamp);
		// Setting AssetType abbreviation to be longer than 50 chars.
		requestAssetType.setAttributes(assetTypeAttrAll);
		
		int expectedRespCode = 201;
		
		String payload = baseHelper.toJSONString(requestAssetType);
		ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
		AssetType responseAssetType = baseHelper.toClassObject(conRespPost.getRespBody(), AssetType.class);
		
		assertEquals("Unexpected response code", requestAssetType, responseAssetType);
	}
}
