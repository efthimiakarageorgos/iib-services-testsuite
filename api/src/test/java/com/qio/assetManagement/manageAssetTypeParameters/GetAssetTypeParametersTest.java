package com.qio.assetManagement.manageAssetTypeParameters;

import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeParameterAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeParameter;
import io.qio.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

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
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// The following test cases go here:
	// issuetype = Test AND issue in (linkedIssues(RREHM-1192)) AND issue in (linkedIssues(RREHM-952), linkedIssues(RREHM-953))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1259 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForANonExistingAssetType() {

		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(404, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
	}

	// RREHM-1255 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForAnExistingAssetTypeThatHasNoParametersConfigured() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeParameterId = "ThisDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeParameterId, apiRequestHelper, assetTypeParameterAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "No Parameters are Associated with a given Asset Type", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-1254 ()
	@Test
	public void shouldBeAbleToGetAnAssetTypeParameterUsingExistingAssetTypeIdAndParameterId() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			String createdParameterId = APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref());
			AssetTypeParameter committedAssetTypeParameter = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, createdParameterId, apiRequestHelper, assetTypeParameterAPI,
					AssetTypeParameter.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeParameter, committedAssetTypeParameter);
		}
	}

	// RREHM-1258 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithNoParameters() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeParameterAPI,
				AssetTypeParameter.class);

		committedAssetTypeParameters = committedAssetTypeParameters.size() == 0 ? null : committedAssetTypeParameters;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getParameters(), committedAssetTypeParameters);
	}

	// RREHM-1257 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithAllParameters() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeParameterAPI,
				AssetTypeParameter.class);

		Collections.sort(committedAssetTypeParameters);
		Collections.sort(responseAssetType.getParameters());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeParameters, responseAssetType.getParameters());
	}

	/*
	 * POSITIVE TESTS END
	 */

}
