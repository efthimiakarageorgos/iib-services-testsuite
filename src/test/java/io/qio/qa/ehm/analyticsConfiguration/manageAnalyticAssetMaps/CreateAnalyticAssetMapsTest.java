/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */

package io.qio.qa.ehm.analyticsConfiguration.manageAnalyticAssetMaps;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.ehm.apiHelpers.analytics.MAnalyticAssetMapAPIHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.ehm.common.AnalyticsUtil;
import io.qio.qa.lib.ehm.common.AssetUtil;
import io.qio.qa.lib.ehm.model.analyticAssetMap.AnalyticAssetMap;
import io.qio.qa.lib.ehm.model.analyticAssetMap.AssetTemplateModelAttribute;
import io.qio.qa.lib.ehm.model.analyticAssetMap.helper.AnalyticAssetMapHelper;
import io.qio.qa.lib.ehm.model.asset.AssetResponse;
import io.qio.qa.lib.exception.ServerResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CreateAnalyticAssetMapsTest extends BaseTestSetupAndTearDown {

	private static MAnalyticAssetMapAPIHelper analyticAssetMapAPI;
	private AnalyticAssetMapHelper analyticAssetMapHelper;
	private AnalyticAssetMap requestAnalyticAssetMap;
	private AnalyticAssetMap responseAnalyticAssetMap;
	private AssetResponse responseAsset;
	private static AssetUtil assetUtil;
	private ServerResponse serverResp;

	private static String assetId;
	private final int FIRST_ELEMENT = 0;

	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("analytics");
		analyticAssetMapAPI = new MAnalyticAssetMapAPIHelper();
		// TO DO
		// WE want to create an asset here that goes against the assettype id we use for the analytic asset map
		// testing - BUT WE WANT TO HAVE ONLY A LINE OR TWO, NOT LOTS OF IMPORTS AND DECLARATIONS
		assetId = "56fca9d633c5721c670641ef";
	}

	@Before
	public void initSetupBeforeEveryTest() {
		analyticAssetMapHelper = new AnalyticAssetMapHelper();
		requestAnalyticAssetMap = new AnalyticAssetMap();
		responseAnalyticAssetMap = new AnalyticAssetMap();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		baseCleanUpAfterAllTests(analyticAssetMapAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-2202")) and issue in linkedIssues("RREHM-1832")

	/*
	 * NEGATIVE TESTS START
	 */
	// RREHM-2401 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentNonValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset("NonExistentId");
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Invalid Asset id in the request","links":[]}]
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Invalid Asset id in the request", serverResp);
	}

	// RREHM-2400 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset("572bbadb13b38458022a33e6"); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Invalid Asset id in the request","links":[]}]
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Invalid Asset id in the request", serverResp);
	}
	
	// RREHM-2399 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset(assetId); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("572bbadb13b38458022a33e6"); // Non existent but valid id format

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Analytic 572bbadb13b38458022a33e6 not found.","links":[]}]
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Analytic 572bbadb13b38458022a33e6 not found.", serverResp);
	}
	
	// RREHM-2398 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentNonValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset(assetId); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("NonExistentId"); // Non existent non valid id format

		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.IllegalArgumentException", "invalid ObjectId [NonExistentId]", serverResp);
	}
	
	// RREHM-2420 ()
	//BUG: RREHM-2417
	@Test
	public void shouldNotCreateAnalyticAssetMapWhenAssetTypeAttributeInAssetTemplateModelAttributeIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributesWithAssetAttributeLink(AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String analyticAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).getAnalyticAttribute();
		requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).setAssetTypeAttribute(analyticAttribute);
		
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", "TODO", serverResp);
	}
	
	// RREHM-2421 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticAttributeInAssetTemplateModelAttributeIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributesWithAssetAttributeLink(AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String assetTypeAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).getAssetTypeAttribute();

		requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).setAnalyticAttribute(assetTypeAttribute);
		
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", "TODO", serverResp);
	}
	
	// RREHM-2418 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticInputInAnalyticInputParameterIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String assetTypeParameter = requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).getAssetTypeParameter();
		requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).setAnalyticInput(assetTypeParameter);
		
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		//CustomAssertions.assertServerError(400, "DONOTKNOWYET", "TODO", serverResp);
	}
	
	// RREHM-2419 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetTypeParameterInAnalyticInputParameterIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String analyticParameter = requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).getAnalyticInput();
		requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).setAssetTypeParameter(analyticParameter);
		
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		//CustomAssertions.assertServerError(400, "DONOTKNOWYET", "TODO", serverResp);
	}
	
	// RREHM-2422 ()
	
	// RREHM-2423 ()
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	
	/*
	 * POSITIVE TESTS START
	 */
	
	// RREHM-2396 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithAnalyticInputParametersOnly() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = APITestUtil.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap = APITestUtil.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
	
	// RREHM-2397 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithAssetTemplateModelAttributesOnly() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = APITestUtil.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap = APITestUtil.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
	
	// RREHM-2229 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithLinkedAndNotLinkedAttributesAndAnalyticInputParameters() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap, AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		
		requestAnalyticAssetMap.setEnabled(null);
		//requestAnalyticAssetMap.setLifetimeStart("2014-03-01T14:30:01.000Z");
		requestAnalyticAssetMap.setLifetimeEnd("2033-01-01T04:30:01.000Z");

		//logger.info("AAA " + requestAnalyticAssetMap.getEnabled());
		List<AssetTemplateModelAttribute> assetTemplateModelAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes();
		assetTemplateModelAttribute.get(0).setValue("0.5");
		requestAnalyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttribute);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
				
		String analyticAssetMapId = APITestUtil.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		//idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap = APITestUtil.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
}
