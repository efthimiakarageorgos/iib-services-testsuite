package com.qio.assetManagement.manageAssetTypeAttributes;

import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAttributeAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeAttribute;
import io.qio.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

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

	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-950), linkedIssues(RREHM-951))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1268 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForANonExistingAssetType() {

		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(404, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
	}

	// RREHM-1253 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForAnExistingAssetTypeThatHasNoAttributesConfigured() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeAttributeId = "ThisDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeAttributeId, apiRequestHelper, assetTypeAttributeAPI, ServerResponse.class);
		
		//TODO - JEET 
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
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAttributeAPI,
				AssetTypeAttribute.class);

		Collections.sort(committedAssetTypeAttributes);
		Collections.sort(responseAssetType.getAttributes());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeAttributes, responseAssetType.getAttributes());
	}

	// RREHM-1251 ()
	@Test
	public void shouldBeAbleToGetAnAssetTypeAttributeUsingExistingAssetTypeIdAndAttributeId() {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeAttribute responseAssetTypeAttribute : responseAssetType.getAttributes()) {
			String createdAttributeId = APITestUtil.getElementId(responseAssetTypeAttribute.get_links().getSelfLink().getHref());
			AssetTypeAttribute committedAssetTypeAttribute = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, createdAttributeId, apiRequestHelper, assetTypeAttributeAPI,
					AssetTypeAttribute.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeAttribute, committedAssetTypeAttribute);
		}
	}

	// RREHM-1256 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeAttributesForAnExistingAssetTypeIdWithNoAttributes() {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId, apiRequestHelper, assetTypeAttributeAPI,
				AssetTypeAttribute.class);

		committedAssetTypeAttributes = committedAssetTypeAttributes.size() == 0 ? null : committedAssetTypeAttributes;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getAttributes(), committedAssetTypeAttributes);
	}

	/*
	 * POSITIVE TESTS END
	 */
}
