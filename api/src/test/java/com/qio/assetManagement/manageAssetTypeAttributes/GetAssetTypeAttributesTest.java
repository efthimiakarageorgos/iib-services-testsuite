package com.qio.assetManagement.manageAssetTypeAttributes;

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
import com.qio.lib.apiHelpers.assetType.MAssetTypeAttributeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.util.common.APITestUtil;

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
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// This file should contain these tests
	// issuetype = Test AND issue in (linkedIssues(RREHM-1193)) AND issue in (linkedIssues(RREHM-950), linkedIssues(RREHM-951))

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1268 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForANonExistingAssetType() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, assetTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL",
				serverResp);
	}

	// RREHM-1253 ()
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForAnExistingAssetTypeThatHasNoAttributesConfigured() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		String invalidAssetTypeAttributeId = "ThisDoesNotExist";
		serverResp = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId, invalidAssetTypeAttributeId, apiRequestHelper,
				assetTypeAttributeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"No Parameters are Associated with a given Asset Type", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-1267 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeAttributesForAnExistingAssetTypeIdWithAllAttributes() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId,
				apiRequestHelper, assetTypeAttributeAPI, AssetTypeAttribute.class);

		Collections.sort(committedAssetTypeAttributes);
		Collections.sort(responseAssetType.getAttributes());

		CustomAssertions.assertRequestAndResponseObj(committedAssetTypeAttributes, responseAssetType.getAttributes());
	}

	// RREHM-1251 ()
	@Test
	public void shouldBeAbleToGetAnAssetTypeAttributeUsingExistingAssetTypeIdAndAttributeId() throws JsonGenerationException, JsonMappingException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		for (AssetTypeAttribute responseAssetTypeAttribute : responseAssetType.getAttributes()) {
			String createdAttributeId = APITestUtil.getElementId(responseAssetTypeAttribute.get_links().getSelfLink().getHref());
			AssetTypeAttribute committedAssetTypeAttribute = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeId,
					createdAttributeId, apiRequestHelper, assetTypeAttributeAPI, AssetTypeAttribute.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetTypeAttribute, committedAssetTypeAttribute);
		}
	}

	// RREHM-1256 ()
	@Test
	public void shouldBeAbleToGetAllAssetTypeAttributesForAnExistingAssetTypeIdWithNoAttributes() throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {

		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI,
				AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		List<AssetTypeAttribute> committedAssetTypeAttributes = APITestUtil.getListResponseObjForRetrieve(microservice, environment, assetTypeId,
				apiRequestHelper, assetTypeAttributeAPI, AssetTypeAttribute.class);

		committedAssetTypeAttributes = committedAssetTypeAttributes.size() == 0 ? null : committedAssetTypeAttributes;
		CustomAssertions.assertRequestAndResponseObjForNullEqualityCheck(responseAssetType.getAttributes(), committedAssetTypeAttributes);
	}

	/*
	 * POSITIVE TESTS END
	 */
}
