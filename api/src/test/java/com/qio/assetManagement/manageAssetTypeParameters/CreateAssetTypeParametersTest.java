package com.qio.assetManagement.manageAssetTypeParameters;

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
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeParameter;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.testHelper.TestHelper;


public class CreateAssetTypeParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private String assetTypeId;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	
	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests(Microservice.ASSET.toString());
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		responseAssetType = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment,
				apiRequestHeaders, assetTypeAPI, AssetType.class);
		assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}
	
	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}
	
	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in  linkedIssues("RREHM-901")
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1099 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItHasAbbrSimilarToExistingParameterAbbr()
			throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		List<AssetTypeParameter> assetTypeParameterWithSameAbbr = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		String abbrForExistingParameter = requestAssetType.getParameters().get(FIRST_ELEMENT).getAbbreviation();
		assetTypeParameterWithSameAbbr.get(FIRST_ELEMENT).setAbbreviation(abbrForExistingParameter);
		assetTypeParameterWithSameAbbr.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithSameAbbr);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}
	
	// RREHM-1097 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItHasAbbrLongerThan255Chars()
			throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		List<AssetTypeParameter> assetTypeParameterWithAbbrLongerThan255Chars = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithAbbrLongerThan255Chars.get(FIRST_ELEMENT).setAbbreviation(TestHelper.TWOFIFTYSIX_CHARS);
		assetTypeParameterWithAbbrLongerThan255Chars.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithAbbrLongerThan255Chars);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}
	
	// RREHM-1095 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrHasSpaces() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeParameter> assetTypeParameterWithAbbrContainingSpaces = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithAbbrContainingSpaces.get(FIRST_ELEMENT).setAbbreviation("Abrr has a space");
		assetTypeParameterWithAbbrContainingSpaces.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithAbbrContainingSpaces);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation must not contain Spaces", serverResp);
	}
	
	// RREHM-932 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsBaseuomIsBlank() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeParameter> assetTypeParameterWithBlankBaseuom = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithBlankBaseuom.get(FIRST_ELEMENT).setbaseuom("");
		assetTypeParameterWithBlankBaseuom.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithBlankBaseuom);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter BaseUom Should not be Empty or Null", serverResp);
	}
	
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsBaseuomIsNull() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeParameter> assetTypeParameterWithNullBaseuom = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithNullBaseuom.get(FIRST_ELEMENT).setbaseuom(null);
		assetTypeParameterWithNullBaseuom.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithNullBaseuom);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	// RREHM-905 ()
	
	// RREHM-845 ()
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrIsBlank() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeParameter> assetTypeParameterWithBlankAbbr = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithBlankAbbr.get(FIRST_ELEMENT).setAbbreviation("");
		assetTypeParameterWithBlankAbbr.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithBlankAbbr);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Parameter Abbreviation Should not be Empty or Null", serverResp);
	}
	
	@Test
	public void shouldNotBeAllowedToAddNewParameterWhenItsAbbrIsNull() throws JsonGenerationException,
			JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		List<AssetTypeParameter> assetTypeParameterWithNullAbbr = assetTypeHelper
				.getAssetTypeParameterWithInputDataType(ParameterDataType.Float);
		assetTypeParameterWithNullAbbr.get(FIRST_ELEMENT).setAbbreviation(null);
		assetTypeParameterWithNullAbbr.addAll(requestAssetType.getParameters());

		requestAssetType.setParameters(assetTypeParameterWithNullAbbr);
		serverResp = TestHelper.getResponseObjForUpdate(baseHelper, requestAssetType, microservice, environment,
				assetTypeId, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-1098 ()
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1290 ()

	// RREHM-1100 ()
	
	// RREHM-1075 ()
	
	// RREHM-1616 ()
	
	
	
	/*
	 * POSITIVE TESTS END
	 */

}
