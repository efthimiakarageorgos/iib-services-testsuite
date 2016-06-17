package com.qio.assetManagement.manageAssetTypeParameters;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeParameter;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.util.common.APITestUtil;

public class CreateAssetTypeParametersTest extends BaseTestSetupAndTearDown {

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
	public void initSetupBeforeEveryTest() {
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		assetTypeParameterId = APITestUtil.getElementId(responseAssetType.getParameters().get(FIRST_ELEMENT).get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in linkedIssues("RREHM-901")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1099 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItHasAbbrSimilarToExistingParameterAbbr() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		String abbrForExistingFirstParameter = existingAssetTypeParameters.get(FIRST_ELEMENT).getAbbreviation();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithSameAbbr = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithSameAbbr.setAbbreviation(abbrForExistingFirstParameter);
		existingAssetTypeParameters.add(assetTypeParameterWithSameAbbr);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-1097 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItHasAbbrLongerThan255Chars() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithAbbrLongerThan255Chars = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithAbbrLongerThan255Chars.setAbbreviation(APITestUtil.TWOFIFTYSIX_CHARS);
		existingAssetTypeParameters.add(assetTypeParameterWithAbbrLongerThan255Chars);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}

	// RREHM-1095 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrHasSpaces() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithAbbrContainingSpaces = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithAbbrContainingSpaces.setAbbreviation("Abrr has a space");
		existingAssetTypeParameters.add(assetTypeParameterWithAbbrContainingSpaces);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-932 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsBaseuomIsBlank() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithBlankBaseuom = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithBlankBaseuom.setBaseuom("");
		existingAssetTypeParameters.add(assetTypeParameterWithBlankBaseuom);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsBaseuomIsNull() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithNullBaseuom = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithNullBaseuom.setBaseuom(null);
		existingAssetTypeParameters.add(assetTypeParameterWithNullBaseuom);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	// RREHM-905 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItHasSameAbbrAsExistingParameter() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		String abbrForExistingFirstParameter = existingAssetTypeParameters.get(FIRST_ELEMENT).getAbbreviation();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithSameAbbr = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithSameAbbr.setAbbreviation(abbrForExistingFirstParameter);
		existingAssetTypeParameters.add(assetTypeParameterWithSameAbbr);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-845 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrIsBlank() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithBlankAbbr = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithBlankAbbr.setAbbreviation("");
		existingAssetTypeParameters.add(assetTypeParameterWithBlankAbbr);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrIsNull() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithNullAbbr = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithNullAbbr.setAbbreviation(null);
		existingAssetTypeParameters.add(assetTypeParameterWithNullAbbr);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-1098 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsDatatypeIsInvalid() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithInvalidDatatype = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithInvalidDatatype.setDatatype("FicticiousDataType");
		existingAssetTypeParameters.add(assetTypeParameterWithInvalidDatatype);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsDatatypeIsBlank() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);

		AssetTypeParameter assetTypeParameterWithBlankDatatype = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithBlankDatatype.setDatatype("");
		existingAssetTypeParameters.add(assetTypeParameterWithBlankDatatype);

		requestAssetType.setParameters(existingAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1290 ()
	// @Test
	// public void
	// shouldBeAllowedToUpdateAssetTypeWithNewParameterThatIsCopiedOverFromAnotherAssetType()
	// throws JsonGenerationException,
	// JsonMappingException, IOException, IllegalAccessException,
	// IllegalArgumentException, InvocationTargetException,
	// NoSuchMethodException,
	// SecurityException {
	//
	// AssetTypeParameter existingFirstAssetTypeParameter =
	// requestAssetType.getParameters().get(FIRST_ELEMENT);
	// existingFirstAssetTypeParameter.setId(assetTypeParameterId);
	//
	// AssetType
	// requestAssetTypeContainingSameParameterAsAnotherExistingAssetType =
	// assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
	// AssetType
	// responseAssetTypeContainingSameParameterAsAnotherExistingAssetType =
	// TestHelper.getResponseObjForCreate(baseHelper,
	// requestAssetTypeContainingSameParameterAsAnotherExistingAssetType,
	// microservice, oauthMicroservice, environment, apiRequestHeaders, assetTypeAPI,
	// AssetType.class);
	// String assetTypeIdForNewlyCreatedAssetType =
	// TestHelper.getElementId(responseAssetTypeContainingSameParameterAsAnotherExistingAssetType
	// .get_links().getSelfLink().getHref());
	// idsForAllCreatedElements.add(assetTypeIdForNewlyCreatedAssetType);
	//
	// requestAssetTypeContainingSameParameterAsAnotherExistingAssetType.setParameters(new
	// ArrayList<AssetTypeParameter>() {
	// {
	// add(existingFirstAssetTypeParameter);
	// }
	// });
	//
	// responseAssetType = TestHelper.getResponseObjForUpdate(baseHelper,
	// requestAssetTypeContainingSameParameterAsAnotherExistingAssetType,
	// microservice, oauthMicroservice, environment, assetTypeIdForNewlyCreatedAssetType,
	// apiRequestHeaders, assetTypeAPI, AssetType.class);
	// CustomAssertions.assertRequestAndResponseObj(200,
	// TestHelper.responseCodeForInputRequest,
	// requestAssetTypeContainingSameParameterAsAnotherExistingAssetType,
	// responseAssetType);
	//
	// AssetType updatedAssetType =
	// TestHelper.getResponseObjForRetrieve(baseHelper, microservice, oauthMicroservice,
	// environment, assetTypeIdForNewlyCreatedAssetType,
	// apiRequestHeaders, assetTypeAPI, AssetType.class);
	// CustomAssertions.assertRequestAndResponseObj(responseAssetType,
	// updatedAssetType);
	// }

	// RREHM-1100 ()
	@Test
	public void shouldBeAllowedToAddNewParameterWithinTheSameAssetTypeWhenItsAbbrIsUnique() {

		AssetTypeParameter existingFirstAssetTypeParameter = requestAssetType.getParameters().get(FIRST_ELEMENT);
		String abbrForExistingFirstParameter = existingFirstAssetTypeParameter.getAbbreviation();
		AssetTypeParameter copyOfExistingFirstAsetTypeParameter = new AssetTypeParameter(existingFirstAssetTypeParameter);

		existingFirstAssetTypeParameter.setAbbreviation(abbrForExistingFirstParameter + "NEW");
		existingFirstAssetTypeParameter.setId(assetTypeParameterId);
		copyOfExistingFirstAsetTypeParameter.setAbbreviation(abbrForExistingFirstParameter + "2");

		requestAssetType.setParameters(new ArrayList<AssetTypeParameter>() {
			{
				add(existingFirstAssetTypeParameter);
				add(copyOfExistingFirstAsetTypeParameter);
			}
		});

		responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	// RREHM-1075 ()
	@Test
	public void shouldBeAllowedToAddNewParameterWithSpecialCharsInItsAbbr() {

		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			initSetupBeforeEveryTest();
			AssetTypeParameter existingFirstAssetTypeParameter = requestAssetType.getParameters().get(FIRST_ELEMENT);
			String abbrForExistingFirstParameter = existingFirstAssetTypeParameter.getAbbreviation();

			AssetTypeParameter copyOfExistingFirstAsetTypeParameter = new AssetTypeParameter(existingFirstAssetTypeParameter);

			existingFirstAssetTypeParameter.setAbbreviation(specialChar + abbrForExistingFirstParameter);
			existingFirstAssetTypeParameter.setId(assetTypeParameterId);
			copyOfExistingFirstAsetTypeParameter.setAbbreviation(specialChar + abbrForExistingFirstParameter + "SpecialChar");

			requestAssetType.setParameters(new ArrayList<AssetTypeParameter>() {
				{
					add(existingFirstAssetTypeParameter);
					add(copyOfExistingFirstAsetTypeParameter);
				}
			});

			responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

			AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
		}
	}

	// RREHM-1616 ()
	@Test
	public void shouldBeAllowedToAddNewParameterWithBlankDescription() {

		List<AssetTypeParameter> existingAssetTypeParameters = requestAssetType.getParameters();
		String abbrForExistingFirstParameter = existingAssetTypeParameters.get(FIRST_ELEMENT).getAbbreviation();
		existingAssetTypeParameters.get(FIRST_ELEMENT).setId(assetTypeParameterId);
		existingAssetTypeParameters.get(FIRST_ELEMENT).setDescription("");

		AssetTypeParameter assetTypeParameterWithSpecialCharsAbbr = assetTypeHelper.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithSpecialCharsAbbr.setAbbreviation(abbrForExistingFirstParameter + APITestUtil.SPECIAL_CHARS);
		assetTypeParameterWithSpecialCharsAbbr.setDescription("");
		existingAssetTypeParameters.add(assetTypeParameterWithSpecialCharsAbbr);

		responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */

}
