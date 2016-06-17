
package com.qio.analyticsConfiguration.manageAnalyticAssetMaps;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
	// RREHM-xxx ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentNonValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		// The line below is commented as we cannot create a AAM if it does not have attributes and pars - awaiting calrification
		// requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset("NonExistentId");
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Invalid Asset id in the request","links":[]}]
		// serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Invalid Asset id in the request", serverResp);
	}

	// RREHM-xxx ()
<<<<<<< Updated upstream
	@Test
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		// The line below is commented as we cannot create a AAM if it does not have attributes and pars - awaiting calrification
		// requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset("572bbadb13b38458022a33e6"); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("ThisIdDoesNotExist");

		// TO DO --- JEET: Response Body makes the below call fail:
		// [
		// {
		// "timestamp": 1464828156482,
		// "status": 500,
		// "error": "Internal Server Error",
		// "exception": "java.lang.IllegalArgumentException",
		// "message": "invalid ObjectId [57488a65e4b0c90b07bf5d47X]",
		// "path": "/analyticassetmap/57488a65e4b0c90b07bf5d47X"
		// }
		// serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(500, "xxxx", "invalid ObjectId [ThisIdDoesNotExist]", serverResp);
=======
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentValid() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
			
		//The line below is commented as we cannot create a AAM if it does not have attributes and pars - awaiting calrification
		//requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap, AnalyticsUtil.analyticInputsMap);
		
		requestAnalyticAssetMap.setAsset("572bbadb13b38458022a33e6"); //Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("ThisIdDoesNotExist"); 
			
		//TO DO --- JEET: Response Body makes the below call fail:
		//[
//		  {
//		  "timestamp": 1464828156482,
//		  "status": 500,
//		  "error": "Internal Server Error",
//		  "exception": "java.lang.IllegalArgumentException",
//		  "message": "invalid ObjectId [57488a65e4b0c90b07bf5d47X]",
//		  "path": "/analyticassetmap/57488a65e4b0c90b07bf5d47X"
//		}
		//serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		//CustomAssertions.assertServerError(500, "xxxx", "invalid ObjectId [ThisIdDoesNotExist]", serverResp);
>>>>>>> Stashed changes
	}

	// RREHM-xxx ()
<<<<<<< Updated upstream
	@Test
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		// The line below is commented as we cannot create a AAM if it does not have attributes and pars - awaiting calrification
		// requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);

=======
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentValid() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		//The line below is commented as we cannot create a AAM if it does not have attributes and pars - awaiting calrification
		//requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap, AnalyticsUtil.analyticInputsMap);
		
>>>>>>> Stashed changes
		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic("572bbadb13b38458022a33e6"); // Non existent but valid id format

		// JEET: Response Body makes the below call fail: [{"logref":"error","message":"Analytic 572bbadb13b38458022a33e6 not found.","links":[]}]
		// serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Analytic 572bbadb13b38458022a33e6 not found.", serverResp);
	}

	// RREHM-xxx ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentNonValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic("NonExistentId");

		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.IllegalArgumentException", "invalid ObjectId [NonExistentId]", serverResp);
	}

	// RREHM-xxx ()
<<<<<<< Updated upstream
	@Test
	public void shouldNotCreateAnalyticAssetMapWithAnalyticInputParametersOnly() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

=======
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWithAnalyticInputParametersOnly() throws JsonGenerationException, JsonMappingException, IOException,
		IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
				
>>>>>>> Stashed changes
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-xxx ()
<<<<<<< Updated upstream
	@Test
	public void shouldNotCreateAnalyticAssetMapWithAssetTemplateModelAttributesOnly() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(AnalyticsUtil.analyticAttributesWithoutLinks,
				AnalyticsUtil.analyticAttributesWithLinksMap);

=======
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWithAssetTemplateModelAttributesOnly() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
			
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap);
			
>>>>>>> Stashed changes
		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-xxx ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWithoutAssetTemplateModelAttributesAndAnalyticInputParameters() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-xxx ()
	@Test
<<<<<<< Updated upstream
	public void shouldCreateAnalyticAssetMapWithLinkedAndNotLinkedAttributesAndAnalyticInputParameters() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		responseAsset = assetUtil.createAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		// assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());

		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap,
				AnalyticsUtil.analyticInputsMap);
=======
	public void shouldCreateAnalyticAssetMapWithLinkedAndNotLinkedAttributesAndAnalyticInputParameters() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//responseAsset = assetUtil.createAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		//assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap, AnalyticsUtil.analyticInputsMap);
>>>>>>> Stashed changes
		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		// requestAnalyticAssetMap.setEnabled(null);
		requestAnalyticAssetMap.setLifetimeStart("2014-03-01T14:30:01.000Z");
		requestAnalyticAssetMap.setLifetimeEnd("2017-01-01T04:30:01.000Z");

		logger.info("AAA " + requestAnalyticAssetMap.getEnabled());
		List<AssetTemplateModelAttribute> assetTemplateModelAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes();
		assetTemplateModelAttribute.get(0).setValue("0.5");
		requestAnalyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttribute);

		responseAnalyticAssetMap = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = APITestUtil.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		// idsForAllCreatedElements.add(analyticAssetMapId);

		// AnalyticAssetMap committedAnalyticAssetMap = APITestUtil.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI,
		// AnalyticAssetMap.class);
		// CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
	/*
	 * POSITIVE TESTS END
	 */
}
