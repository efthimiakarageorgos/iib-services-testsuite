/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.assetManagement.manageAssetTypeAttributes;

import java.util.Collections;
import java.util.List;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hbc.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.hbc.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAttributeAPIHelper;
import com.hbc.qa.lib.ehm.model.assetType.AssetType;
import com.hbc.qa.lib.ehm.model.assetType.AssetTypeAttribute;
import com.hbc.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
//import com.hbc.qa.lib.iib.common.APITestUtil;
import com.hbc.qa.lib.assertions.CustomAssertions;
import com.hbc.qa.lib.exception.ServerResponse;
import com.hbc.qa.lib.common.MAbstractAPIHelper;

public class GetAssetTypeAttributesTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private static MAssetTypeAttributeAPIHelper assetTypeAttributeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
		assetTypeAttributeAPI = new MAssetTypeAttributeAPIHelper();
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
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-950), linkedIssues(RREHM-951))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1268 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForANonExistingAssetType() {

		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(404, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
	}

	// RREHM-1253 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForAnExistingAssetTypeThatHasNoAttributesConfigured() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeAttributeId = "ThisDoesNotExist";
		serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeAttributeId, apiRequestHelper, assetTypeAttributeAPI, ServerResponse.class);
		
		//TODO
		//If the message does not much it fails instead of giving the a nicer message (same as when the code does not match - not a bid deal though)
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "No Attributes are Associated with a given Asset Type", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-1267 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeAttributesForAnExistingAssetTypeIdWithAllAttributes() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = MAbstractAPIHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAttributeAPI,
				AssetTypeAttribute.class);

		Collections.sort(committedAssetTypeAttributes);
		Collections.sort(responseAssetType.getAttributes());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeAttributes, responseAssetType.getAttributes());
	}

	// RREHM-1251 ()
	@Test
	public void shouldBeAbleToGetAnAssetTypeAttributeUsingExistingAssetTypeIdAndAttributeId() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeAttribute responseAssetTypeAttribute : responseAssetType.getAttributes()) {
			String createdAttributeId = responseAssetTypeAttribute.getAttributeId();
			AssetTypeAttribute committedAssetTypeAttribute = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetTypeId, createdAttributeId, apiRequestHelper, assetTypeAttributeAPI,
					AssetTypeAttribute.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeAttribute, committedAssetTypeAttribute);
		}
	}

	// RREHM-1256 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeAttributesForAnExistingAssetTypeIdWithNoAttributes() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = MAbstractAPIHelper.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = responseAssetType.getAssetTypeId();
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = MAbstractAPIHelper.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAttributeAPI,
				AssetTypeAttribute.class);

		committedAssetTypeAttributes = committedAssetTypeAttributes.size() == 0 ? null : committedAssetTypeAttributes;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getAttributes(), committedAssetTypeAttributes);
	}

	/*
	 * POSITIVE TESTS END
	 */
}
