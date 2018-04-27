/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.assetManagement.manageAssetTypeParameters;

import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import com.hbc.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.hbc.qa.lib.ehm.apiHelpers.assetType.MAssetTypeParameterAPIHelper;
import com.hbc.qa.lib.ehm.model.assetType.AssetType;
import com.hbc.qa.lib.ehm.model.assetType.AssetTypeParameter;
import com.hbc.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
//import com.hbc.qa.lib.iib.common.APITestUtil;
import com.hbc.qa.lib.assertions.CustomAssertions;
import com.hbc.qa.lib.exception.ServerResponse;
import com.hbc.qa.lib.common.MAbstractAPIHelper;

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

	// Matching test cases in Test Case Management (Jira/Zephyr):
	// issuetype = Test AND issue in (linkedIssues(RREHM-1192)) AND issue in (linkedIssues(RREHM-952), linkedIssues(RREHM-953))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1259 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForANonExistingAssetType() {
		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(404, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
	}

	// RREHM-1255 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetParametersForAnExistingAssetTypeThatHasNoParametersConfigured() {
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		
		String assetTypeId=responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeParameterId = "ThisDoesNotExist";
		serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeParameterId, apiRequestHelper, assetTypeParameterAPI, ServerResponse.class);
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
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		
		String assetTypeId=responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			String createdParameterId = responseAssetTypeParameter.getParameterId();
			AssetTypeParameter committedAssetTypeParameter = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, createdParameterId, apiRequestHelper, assetTypeParameterAPI,
					AssetTypeParameter.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeParameter, committedAssetTypeParameter);
		}
	}

	// RREHM-1258 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithNoParameters() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		
		String assetTypeId=responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = MAbstractAPIHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeParameterAPI,
				AssetTypeParameter.class);

		committedAssetTypeParameters = committedAssetTypeParameters.size() == 0 ? null : committedAssetTypeParameters;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getParameters(), committedAssetTypeParameters);
	}

	// RREHM-1257 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeParametersForAnExistingAssetTypeIdWithAllParameters() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		
		String assetTypeId=responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeParameter> committedAssetTypeParameters = MAbstractAPIHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeParameterAPI,
				AssetTypeParameter.class);

		Collections.sort(committedAssetTypeParameters);
		Collections.sort(responseAssetType.getParameters());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeParameters, responseAssetType.getParameters());
	}

	/*
	 * POSITIVE TESTS END
	 */

}
