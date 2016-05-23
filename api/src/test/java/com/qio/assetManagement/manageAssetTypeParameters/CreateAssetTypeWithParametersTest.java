
package com.qio.assetManagement.manageAssetTypeParameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.testHelper.TestHelper;

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
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in linkedIssues("RREHM-41")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1626 (AssetType Parameter abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to blank
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");

		serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not be Empty or Null", serverResp);
	}

	// RREHM-1626 (AssetType Parameter abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		// Setting AssetType Parameter abbreviation to null, so that it is not
		// sent in the request.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);

		serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-1112 (AssetType Parameter abbreviation is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(TestHelper.TWOFIFTYSIX_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}

	// RREHM-1078 (AssetType Parameter abbreviation has spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenParAbbrHasSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting AssetType Parameter abbreviation to have spaces.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("Abrr has a space");

		serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-633 ()
	@Test
	public void shouldNotCreateAssetTypeWhenDataTypeIsInvalidForOneOfTheInputParameters() throws JsonGenerationException, JsonMappingException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		// Setting data type for one of the parameters to be invalid.
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("FicticiousDataType");

		serverResp = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	// RREHM-929 ()
	// RREHM-930 ()
	
	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterOfFloatDataType() throws JsonGenerationException, JsonMappingException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.Float);
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);

		// RV1: comparing CreatedObject with CreateRequest, along with response
		// codes.
		CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);

		// RV2: comparing CommittedObject with CreatedObject, without the
		// response codes.
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-611 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithParametersOfAllDataType() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();

		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	// RREHM-1077 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterContainingSpecialCharInItsAbbr() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		for (char specialChar : TestHelper.SPECIAL_CHARS.toCharArray()) {
			requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
			String origFirstParamAbbr = requestAssetType.getParameters().get(FIRST_ELEMENT).getAbbreviation();
			requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(specialChar + origFirstParamAbbr);

			responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
					AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

			String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
			idsForAllCreatedElements.add(assetTypeId);

			AssetType committedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper,
					assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
		}
	}

	// RREHM-1614 ()
	@Test
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneParameterHavingNoDescription() throws JsonGenerationException, JsonMappingException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDescription("");

		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType committedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, committedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */

}