package com.qio.assetManagement.manageAssetTypeAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.util.common.APITestUtil;

public class CreateAssetTypeWithAttributesTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	final static Logger logger = Logger.getLogger(CreateAssetTypeWithAttributesTest.class);
	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-41))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-453 (AssetType with two Attributes with same abbreviation)
	@Test
	public void shouldNotCreateAssetTypeWhenTwoAttrsHaveSameAbbr() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();

		// Setting AssetType Attribute abbreviation of first attribute to the
		// value of abbr of second attribute
		String abbrForSecondAttribute = requestAssetType.getAttributes().get(FIRST_ELEMENT + 1).getAbbreviation();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(abbrForSecondAttribute);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-451 (AssetType Attribute abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
			String origFirstAttributeAbbr = requestAssetType.getAttributes().get(FIRST_ELEMENT).getAbbreviation();
			requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(specialChar + origFirstAttributeAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
					ServerResponse.class);

			CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
					"Asset Type Attribute Abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-446 (AssetType Attribute abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("This Abbreviation contains spaces");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-450 (AssetType Attribute abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not be Empty or Null", serverResp);
	}

	// RREHM-481 (AssetType Attribute abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-443 (AssetType Attribute abbreviation is longer than 50 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-456 (AssetType Attribute name is set to blank, i.e. name = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute name should not be empty or null", serverResp);
	}

	// RREHM-479 (AssetType Attribute name is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-452 (AssetType Attribute name is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName(
				"256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Name should be less than 255 characters", serverResp);
	}

	// RREHM-458 ()
	@Test
	public void shouldNotCreateAssetTypeWhenDataTypeIsInvalidForOneOfTheAttributes() throws JsonGenerationException, JsonMappingException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setDatatype("FicticiousDataType");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	// RREHM-1871 ()
	// BUG:RREHM-928
	// @Test
	// public void shouldNotCreateAssetTypeWhenUnitIsEmptyForOneOfTheAttributes() throws JsonGenerationException, JsonMappingException,
	// IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	// requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.String);
	// requestAssetType.getAttributes().get(FIRST_ELEMENT).setUnit("");
	//
	// serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
	// ServerResponse.class);
	//
	// CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
	// "Attribute Unit should not be blank", serverResp);
	// }

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-471 ()
	@Test
	public void shouldBeAllowedToCreateAnAssetTypeWithUniqueAbbrWhenAbbrForOneOfItsAttributesContainsSpecificSpecialChars()
			throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("AAbbr-._");

		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-442 ()
	@Test
	public void shouldBeAllowedToCreateAnAssetTypeWithUniqueAbbrWithAttributesOfAllDatatypes() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();

		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-441 ()
	@Test
	public void shouldBeAllowedToCreateAnAssetTypeWithUniqueAbbrWithOneAttributeOfFloatDataType() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.Float);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);

		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-455 ()
	@Test
	public void shouldBeAllowedToCreateAnAssetTypeWithUniqueAbbrWhenDescriptionForOneOfItsAttributesIsMultiparagraph() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setDescription("This is paragraph 1.\n This is paragraph 2.\nThis is paragraph 3.");

		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);

		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */
}
