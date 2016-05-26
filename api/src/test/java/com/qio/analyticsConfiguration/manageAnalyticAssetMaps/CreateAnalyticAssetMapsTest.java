package com.qio.analyticsConfiguration.manageAnalyticAssetMaps;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.util.common.APITestUtil;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.lib.apiHelpers.analytics.MAnalyticAssetMapAPIHelper;
import com.qio.model.analyticAssetMap.*;
import com.qio.model.analyticAssetMap.helper.AnalyticAssetMapHelper;


public class CreateAnalyticAssetMapsTest extends BaseTestSetupAndTearDown {

	private static MAnalyticAssetMapAPIHelper analyticAssetMapAPI;
	private AnalyticAssetMapHelper analyticAssetMapHelper;
	private AnalyticAssetMap requestAnalyticAssetMap;
	private AnalyticAssetMap responseAnalyticAssetMap;
	private ServerResponse serverResp;
	
	private static ArrayList<String> analyticAttributesWithoutLinks;
	private static ArrayList<String> analyticAttributesWithLinks;
	private static ArrayList<String> analyticInputs;
	
	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("analytics");
		analyticAssetMapAPI = new MAnalyticAssetMapAPIHelper();
		logger.info("In Before class");
	}

	@Before
	public void initSetupBeforeEveryTest() {
		analyticAssetMapHelper = new AnalyticAssetMapHelper();
		requestAnalyticAssetMap = new AnalyticAssetMap();
		responseAnalyticAssetMap = new AnalyticAssetMap();
		serverResp = new ServerResponse();
		logger.info("In Before");
	}

//	@AfterClass
//	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException,
//		 IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
//		 baseCleanUpAfterAllTests(analyticAssetMapAPI);
//	}
	
	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-xxx")) and issue in linkedIssues("RREHM-yy")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-xxx ()
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		//requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();
		
		analyticAttributesWithoutLinks = new ArrayList<String>();
		analyticAttributesWithLinks = new ArrayList<String>();
		analyticAttributesWithoutLinks.add("572bb56613b38458022725a5");
		analyticAttributesWithoutLinks.add("572bb56613b38458022725a4");
		
		analyticInputs = new ArrayList<String>();
		analyticInputs.add("572bb56613b38458022725c6");
		analyticInputs.add("572bb56613b38458022725c8");
		
		//requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(analyticAttributesWithoutLinks, analyticAttributesWithLinks);
		
		// requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(analyticInputs, null);
		
		requestAnalyticAssetMap = analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(analyticAttributesWithoutLinks, analyticAttributesWithLinks, analyticInputs, null);
		logger.info("After");
		serverResp = APITestUtil.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);

		String comment = requestAnalyticAssetMap.getComment();
		logger.info(comment);

		//CustomAssertions.assertServerError(409, null, "Creating tenant failed, as another tenant has same abbreviation.", serverResp);
	}

	
	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	
	/*
	 * POSITIVE TESTS END
	 */
}
