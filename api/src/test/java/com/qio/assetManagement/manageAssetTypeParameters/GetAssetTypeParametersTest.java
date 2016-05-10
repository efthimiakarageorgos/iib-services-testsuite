package com.qio.assetManagement.manageAssetTypeParameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.apiHelpers.assetType.MAssetTypeParameterAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeParameter;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.testHelper.TestHelper;

public class GetAssetTypeParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private static MAssetTypeParameterAPIHelper assetTypeParameterAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
		assetTypeParameterAPI = new MAssetTypeParameterAPIHelper();
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
	// issuetype = Test AND issue in (linkedIssues(RREHM-1192)) AND issue in (linkedIssues(RREHM-952), linkedIssues(RREHM-953))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1259 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForANonExistingAssetType() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = TestHelper.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHeaders, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL",
				serverResp);
	}

	// RREHM-1255 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForAnExistingAssetTypeThatHasNoParametersConfigured() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeParameterId = "ThisDoesNotExist";
		serverResp = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeParameterId, apiRequestHeaders,
				assetTypeParameterAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"No Parameters are Associated with a given Asset Type", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-1254 ()
	@Test
	public void shouldBeAbleToGetAnAssetTypeParameterUsingExistingAssetTypeIdAndParameterId() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			String createdParameterId = TestHelper.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref());
			AssetTypeParameter committedAssetTypeParameter = TestHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId,
					createdParameterId, apiRequestHeaders, assetTypeParameterAPI, AssetTypeParameter.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeParameter, committedAssetTypeParameter);
		}
	}

	// RREHM-1258 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithNoParameters() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = TestHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId,
				apiRequestHeaders, assetTypeParameterAPI, AssetTypeParameter.class);

		committedAssetTypeParameters = committedAssetTypeParameters.size() == 0 ? null : committedAssetTypeParameters;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getParameters(), committedAssetTypeParameters);
	}

	// RREHM-1257 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithAllParameters() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = TestHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI,
				AssetType.class);
		String assetTypeId = TestHelper.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = TestHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId,
				apiRequestHeaders, assetTypeParameterAPI, AssetTypeParameter.class);

		Collections.sort(committedAssetTypeParameters);
		Collections.sort(responseAssetType.getParameters());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeParameters, responseAssetType.getParameters());
	}

	/*
	 * POSITIVE TESTS END
	 */

}
