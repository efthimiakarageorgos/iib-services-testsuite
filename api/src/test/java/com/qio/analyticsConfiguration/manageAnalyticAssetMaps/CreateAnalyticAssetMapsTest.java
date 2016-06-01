package com.qio.analyticsConfiguration.manageAnalyticAssetMaps;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.analytics.MAnalyticAssetMapAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.analyticAssetMap.AnalyticAssetMap;
import com.qio.model.analyticAssetMap.AssetTemplateModelAttribute;
import com.qio.model.analyticAssetMap.helper.AnalyticAssetMapHelper;
import com.qio.model.asset.AssetResponse;
import com.qio.util.common.APITestUtil;
import com.qio.util.common.AnalyticsUtil;
import com.qio.util.common.AssetUtil;

public class CreateAnalyticAssetMapsTest extends BaseTestSetupAndTearDown {

	private static MAnalyticAssetMapAPIHelper analyticAssetMapAPI;
	private AnalyticAssetMapHelper analyticAssetMapHelper;
	private AnalyticAssetMap requestAnalyticAssetMap;
	private AnalyticAssetMap responseAnalyticAssetMap;
	private AssetResponse responseAsset;
	private static AssetUtil assetUtil;
	private ServerResponse serverResp;

	private static String assetId;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("analytics");
		analyticAssetMapAPI = new MAnalyticAssetMapAPIHelper();
		assetUtil = new AssetUtil();
	}

	@Before
	public void initSetupBeforeEveryTest() {
		analyticAssetMapHelper = new AnalyticAssetMapHelper();
		requestAnalyticAssetMap = new AnalyticAssetMap();
		responseAnalyticAssetMap = new AnalyticAssetMap();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(analyticAssetMapAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-xxx")) and issue in linkedIssues("RREHM-yy")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-xxx (WORK IN PROGRESS)
	// @Test
	// public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() {
	//
	// //requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
	// assetId="56fca9d633c5721c670641ef";
	//
	// //requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinks);
	//
	// // requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputs, null);
	//
	// requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
	// AnalyticsUtil.analyticInputsMap);
	// requestAnalyticAssetMap.setAsset(assetId);
	//
	// requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
	//
	// logger.info("After");
	// serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
	//
	// //CustomAssertions.assertServerError(409, null, "Creating tenant failed, as another tenant has same abbreviation.", serverResp);
	// }

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-xxx ()
	@Test
	public void shouldCreateAnalyticAssetMapWithLinkedAndNotLinkedAttributesAndInputParameters() {
		responseAsset = assetUtil.createAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());

		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);
		requestAnalyticAssetMap.setAsset(assetId);

		List<AssetTemplateModelAttribute> assetTemplateModelAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes();
		assetTemplateModelAttribute.get(0).setValue("0.5");
		requestAnalyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttribute);

		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = APITestUtil.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(analyticAssetMapId);

		// GET is not working currently
		AnalyticAssetMap committedAnalyticAssetMap = APITestUtil.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI,
				AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}

	/*
	 * POSITIVE TESTS END
	 */
}
