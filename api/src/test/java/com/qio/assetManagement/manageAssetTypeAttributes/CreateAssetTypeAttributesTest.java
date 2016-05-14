package com.qio.assetManagement.manageAssetTypeAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.testHelper.TestHelper;

public class CreateAssetTypeAttributesTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private String assetTypeId;
	private String assetTypeAttributeId;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.String);
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		assetTypeAttributeId = TestHelper.getElementId(responseAssetType.getAttributes().get(FIRST_ELEMENT).get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-38))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-843 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsAbbrHasSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithAbbrContainingSpaces = assetTypeHelper.getAssetTypeAttributeWithInputDataType(
				AttributeDataType.Float);
		assetTypeAttributeWithAbbrContainingSpaces.setAbbreviation("Abrr has a space");
		existingAssetTypeAttributes.add(assetTypeAttributeWithAbbrContainingSpaces);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-841 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItHasAbbrSimilarToExistingAttributeAbbr() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		String abbrForExistingFirstAttribute = existingAssetTypeAttributes.get(FIRST_ELEMENT).getAbbreviation();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithSameAbbr = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithSameAbbr.setAbbreviation(abbrForExistingFirstAttribute);
		existingAssetTypeAttributes.add(assetTypeAttributeWithSameAbbr);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-842 () -- This needs to be looked into.

	// RREHM-847 () -- Waiting for Effie's reply on this one.
	
	// RREHM-849 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWithSpecialCharsInItsAbbr() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		for (char specialChar : TestHelper.SPECIAL_CHARS.toCharArray()) {
			initSetupBeforeEveryTest();
			List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
			String abbrForExistingFirstAttribute = existingAssetTypeAttributes.get(FIRST_ELEMENT).getAbbreviation();
			existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

			AssetTypeAttribute assetTypeAttributeWithSpecialCharAbbr = assetTypeHelper.getAssetTypeAttributeWithInputDataType(
					AttributeDataType.Float);
			assetTypeAttributeWithSpecialCharAbbr.setAbbreviation(specialChar + abbrForExistingFirstAttribute + "SpecialChar");
			existingAssetTypeAttributes.add(assetTypeAttributeWithSpecialCharAbbr);

			requestAssetType.setAttributes(existingAssetTypeAttributes);
			serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
					ServerResponse.class);
			CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
					"Asset Type Attribute Abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-848 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsDatatypeIsInvalid() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithInvalidDatatype = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithInvalidDatatype.setDatatype("FicticiousDataType");
		existingAssetTypeAttributes.add(assetTypeAttributeWithInvalidDatatype);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsDatatypeIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithBlankDatatype = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithBlankDatatype.setDatatype("");
		existingAssetTypeAttributes.add(assetTypeAttributeWithBlankDatatype);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-840 ()
	// RREHM-846 ()
	// RREHM-844 ()
	// RREHM-838 ()

	/*
	 * POSITIVE TESTS END
	 */
}
