/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */

package io.qio.qa.ehm.assetManagement.manageAssetTypeParameters;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import io.qio.qa.lib.ehm.model.assetType.helper.ParameterDataType;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

public class CreateAssetTypeWithParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

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
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in linkedIssues("RREHM-41")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1626 (AssetType Parameter abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsBlank() {
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to blank
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not be Empty or Null", serverResp);
	}

	// RREHM-1626 (AssetType Parameter abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsNull() {
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to null, so that it is not
		// sent in the request.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-1112 (AssetType Parameter abbreviation is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsLongerThan255Chars() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}

	// RREHM-1078 (AssetType Parameter abbreviation has spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrHasSpaces() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting AssetType Parameter abbreviation to have spaces.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("Abrr has a space");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-633 ()
	@Test
	public void shouldNotCreateAssetTypeWhenDataTypeIsInvalidForOneOfTheInputParameters() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting data type for one of the parameters to be invalid.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("FicticiousDataType");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	// RREHM-929 ()
	@Test
	public void shouldNotCreateAssetTypeWhenTwoParametersHaveSameAbbr() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("ABBRInteger");
		requestAssetType.getParameters().get(FIRST_ELEMENT + 1).setAbbreviation("ABBRInteger");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-930 ()
	@Test
	public void shouldNotCreateAssetTypeWhenParameterBaseuomIsBlank() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotCreateAssetTypeWhenParameterBaseuomIsNull() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterOfFloatDataType() {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.Float);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);

		// RV1: comparing CreatedObject with CreateRequest, along with response
		// codes.
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);

		// RV2: comparing CommittedObject with CreatedObject, without the
		// response codes.
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-611 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithParametersOfAllDataType() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();

		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-1077 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterContainingSpecialCharInItsAbbr() {

		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
			String origFirstParamAbbr = requestAssetType.getParameters().get(FIRST_ELEMENT).getAbbreviation();
			requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(specialChar + origFirstParamAbbr);

			responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

			String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
			idsForAllCreatedElements.add(assetTypeId);

			AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
		}
	}

	// RREHM-1614 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterHavingNoDescription() {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDescription("");

		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */

}