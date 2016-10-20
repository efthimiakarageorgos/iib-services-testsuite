/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.assetManagement.manageAssetTypeAttributes;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeAttribute;
import io.qio.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import io.qio.qa.lib.ehm.model.assetType.helper.AttributeDataType;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

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
	public void initSetupBeforeEveryTest() {
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.String);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		assetTypeAttributeId = APITestUtil.getElementId(responseAssetType.getAttributes().get(FIRST_ELEMENT).get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-38))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-843 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsAbbrHasSpaces() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithAbbrContainingSpaces = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithAbbrContainingSpaces.setAbbreviation("Abrr has a space");
		existingAssetTypeAttributes.add(assetTypeAttributeWithAbbrContainingSpaces);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Attribute Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-841 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItHasSameAbbrAsExistingAttribute() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		String abbrForExistingFirstAttribute = existingAssetTypeAttributes.get(FIRST_ELEMENT).getAbbreviation();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithSameAbbr = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithSameAbbr.setAbbreviation(abbrForExistingFirstAttribute);
		existingAssetTypeAttributes.add(assetTypeAttributeWithSameAbbr);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Attribute Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-842 ()
	@Test
	public void shouldNotBeAllowedToAddTwoNewAttributesWithSameAbbr() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeOne = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		existingAssetTypeAttributes.add(assetTypeAttributeOne);

		AssetTypeAttribute assetTypeAttributeTwo = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		existingAssetTypeAttributes.add(assetTypeAttributeTwo);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Attribute Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	 // RREHM-847 ()
	 // BUG:RREHM-928 - FIXED!
	 @Test
	 public void shouldNotBeAllowedToAddNewAttributeWhenUnitIsEmpty() {
	 
	 List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
	 existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);
	
	 AssetTypeAttribute assetTypeAttributeWithEmptyUnit = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
	 assetTypeAttributeWithEmptyUnit.setUnit("");
	 existingAssetTypeAttributes.add(assetTypeAttributeWithEmptyUnit);
	
	 requestAssetType.setAttributes(existingAssetTypeAttributes);
	 serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
	 CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException",
	 "Attribute unit should not be null as data type is float", serverResp);
	 }

	// RREHM-849 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWithSpecialCharsInItsAbbr() {

		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			initSetupBeforeEveryTest();
			List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
			String abbrForExistingFirstAttribute = existingAssetTypeAttributes.get(FIRST_ELEMENT).getAbbreviation();
			existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

			AssetTypeAttribute assetTypeAttributeWithSpecialCharAbbr = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
			assetTypeAttributeWithSpecialCharAbbr.setAbbreviation(specialChar + abbrForExistingFirstAttribute + "SpecialChar");
			existingAssetTypeAttributes.add(assetTypeAttributeWithSpecialCharAbbr);

			requestAssetType.setAttributes(existingAssetTypeAttributes);
			serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
			CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Attribute Abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-848 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsDatatypeIsInvalid() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithInvalidDatatype = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithInvalidDatatype.setDatatype("FicticiousDataType");
		existingAssetTypeAttributes.add(assetTypeAttributeWithInvalidDatatype);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsDatatypeIsBlank() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithBlankDatatype = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithBlankDatatype.setDatatype("");
		existingAssetTypeAttributes.add(assetTypeAttributeWithBlankDatatype);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	// RREHM-846 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsNameIsLongerThan255Chars() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		// existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithNameLongerThan255Chars = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithNameLongerThan255Chars.setName(APITestUtil.TWOFIFTYSIX_CHARS);
		existingAssetTypeAttributes.add(assetTypeAttributeWithNameLongerThan255Chars);

		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Attribute Name should be less than 255 characters", serverResp);
	}

	// RREHM-844 ()
	@Test
	public void shouldNotBeAllowedToAddNewAttributeWhenItsAbbrIsLongerThan50Chars() {

		List<AssetTypeAttribute> existingAssetTypeAttributes = requestAssetType.getAttributes();
		existingAssetTypeAttributes.get(FIRST_ELEMENT).setId(assetTypeAttributeId);

		AssetTypeAttribute assetTypeAttributeWithAbbrLongerThan50Chars = assetTypeHelper.getAssetTypeAttributeWithInputDataType(AttributeDataType.Float);
		assetTypeAttributeWithAbbrLongerThan50Chars.setAbbreviation(APITestUtil.FIFTYONE_CHARS);
		existingAssetTypeAttributes.add(assetTypeAttributeWithAbbrLongerThan50Chars);
		requestAssetType.setAttributes(existingAssetTypeAttributes);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Attribute Abbreviation Should Less Than 50 Character", serverResp);
	}
	
	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-840 ()
	// RREHM-838 ()

	/*
	 * POSITIVE TESTS END
	 */
}
