package com.qio.assetManagement.manageAssetTypeParameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeParameter;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.testHelper.TestHelper;

public class UpdateAssetTypeParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private String assetTypeId;
	private String assetTypeParameterId;
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
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		assetTypeParameterId = TestHelper.getElementId(responseAssetType.getParameters().get(FIRST_ELEMENT).get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	private String createAssetTypeWithAllParametersOnly() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeIdLocal = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeIdLocal);
		return assetTypeIdLocal;
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in
	// linkedIssues("RREHM-55")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1085 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenAbbrHasSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(TestHelper.getCurrentTimeStamp() + " This has spaces");

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-1093 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenAbbrLongerThan255Chars() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(TestHelper.TWOFIFTYSIX_CHARS);

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}

	// RREHM-935 ()
	@Test
	public void shouldNotBeAllowedToUpdateParametersWhenTheyHaveAbbrSimilarToExistingParameterAbbr() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		// Creating an AssetType with all parameters with one parameter having
		// abbreviation as TRYTODUPLICATEME
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		List<AssetTypeParameter> existingAssetTypeParmaeters = requestAssetType.getParameters();
		for (AssetTypeParameter assetTypeParameter : existingAssetTypeParmaeters) {
			if (assetTypeParameter.getDatatype().equals(ParameterDataType.String.toString()))
				assetTypeParameter.setAbbreviation("TRYTODUPLICATEME");
		}
		requestAssetType.setParameters(existingAssetTypeParmaeters);
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeIdLocal = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeIdLocal);

		// Preparing the AssetTypeParameter for the update request
		List<AssetTypeParameter> updateAssetTypeParameters = new ArrayList<AssetTypeParameter>();
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			if (!responseAssetTypeParameter.getDatatype().equals(ParameterDataType.String.toString())) {
				responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
				responseAssetTypeParameter.set_links(null);
				responseAssetTypeParameter.setAbbreviation("TRYTODUPLICATEME");
				updateAssetTypeParameters.add(responseAssetTypeParameter);
			}
		}

		// Setting up the update request
		requestAssetType.setParameters(updateAssetTypeParameters);
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-934 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenItsAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenItsAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-933 ()
	@Test
	public void shouldNotBeAllowedToUpdateTwoOrMoreParametersWithSameAbbrValue() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}
		responseAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("NEWDUPLICATE");
		responseAssetType.getParameters().get(FIRST_ELEMENT + 1).setAbbreviation("NEWDUPLICATE");

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());
		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-923 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenBaseuomIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom("");

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenBaseuomIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom(null);

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	// RREHM-1084 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenDatatypeIsInvalid() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("FicticiousDataType");

		serverResp = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenDatatypeIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("");

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
	// RREHM-920 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWhenAbbrValueIsUniqueWithinTheScopeOfSameAssetType() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setAbbreviation(responseAssetTypeParameter.getAbbreviation() + "NEW");
			responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	// RREHM-918 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithValidValuesForDescriptionBaseuomAndDatatype() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setBaseuom(responseAssetTypeParameter.getBaseuom() + "NEW");
			responseAssetTypeParameter.setDescription(responseAssetTypeParameter.getDescription() + "NEW");
			responseAssetTypeParameter.setDatatype(ParameterDataType.Integer.toString());
			responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	// RREHM-1076 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithSpecialCharsInItsAbbr() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		for (char specialChar : TestHelper.SPECIAL_CHARS.toCharArray()) {
			String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

			// Preparing the AssetTypeParameter for the update request
			for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
				responseAssetTypeParameter.setAbbreviation(specialChar + responseAssetTypeParameter.getAbbreviation());
				responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
				responseAssetTypeParameter.set_links(null);
			}

			// Setting up the update request
			requestAssetType.setParameters(responseAssetType.getParameters());

			responseAssetType = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
					assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(200, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

			AssetType updatedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHeaders,
					assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
		}
	}

	// RREHM-1615 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithBlankDescription() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setDescription("");
			responseAssetTypeParameter.setId(TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = TestHelper.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, TestHelper.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHeaders,
				assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */

}
