package com.qio.insightManagement.manageInsights;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

import com.qio.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.insights.MInsightAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.insights.MInsightTypeAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.MTenantAPIHelper;
import io.qio.qa.lib.ehm.model.insight.InsightRequest;
import io.qio.qa.lib.ehm.model.insight.InsightResponse;
import io.qio.qa.lib.ehm.model.insight.InsightStatus;
import io.qio.qa.lib.ehm.model.insight.helper.InsightRequestHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

public class CreateInsightsTest extends BaseTestSetupAndTearDown {

	private static MInsightTypeAPIHelper insightTypeAPI;
	private static String insightTypeId;

	private static MTenantAPIHelper tenantAPI;
	private static String tenantId;

	private static MInsightAPIHelper insightAPI;
	private static InsightRequestHelper insightRequestHelper;
	private static InsightRequest requestInsight;
	private static InsightResponse responseInsight;

	private ServerResponse serverResp;

	private static ArrayList<String> idsForAllCreatedInsights;
	private static ArrayList<String> idsForAllCreatedInsightTypes;
	private static ArrayList<String> idsForAllCreatedTenants;

	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("insight");
		insightAPI = new MInsightAPIHelper();
		insightRequestHelper = new InsightRequestHelper();

		insightTypeAPI = new MInsightTypeAPIHelper();
		tenantAPI = new MTenantAPIHelper();

		idsForAllCreatedInsights = new ArrayList<String>();
		idsForAllCreatedInsightTypes = new ArrayList<String>();
		idsForAllCreatedTenants = new ArrayList<String>();

		requestInsight = insightRequestHelper.getInsightWithCreatingInsightTypeAndTenant("WithNoAttributes", null);
		insightTypeId = requestInsight.getInsightType();
		tenantId = requestInsight.getTenant();

		idsForAllCreatedInsightTypes.add(insightTypeId);
		idsForAllCreatedTenants.add(tenantId);
	}

	@Before
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		insightRequestHelper = new InsightRequestHelper();
		requestInsight = new InsightRequest();
		responseInsight = new InsightResponse();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(idsForAllCreatedInsights, insightAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedInsightTypes, insightTypeAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedTenants, tenantAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1190")) and issue in linkedIssues("RREHM-36")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-xxx (Insight title is empty)
	@Test
	public void shouldNotCreateInsightWhenTitleIsEmpty() {
		requestInsight = insightRequestHelper.getInsightWithPredefinedInsightTypeAndTenant(insightTypeId, tenantId);
		requestInsight.setTitle("");

		serverResp = APITestUtil.getResponseObjForCreate(requestInsight, microservice, environment, apiRequestHelper, insightAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}
	

	/*
	 * NEGATIVE TESTS END
	 */

	// TODO: Jeet
	// MORE ASSERTIONS REQS for NEGATIVE:
	// We could use one tenant and asset type for negative tests and a separate pair for positive
	// In that case we could also validate that the tenant used for the positive tests does not have any assets associated with it
	// at the end of each negative test by sending the GET request:
	// {Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid={tenantId}

	/*
	 * POSITIVE TESTS START
	 */

	// TODO: Jeet
	// MORE ASSERTIONS REQS for POSITIVE:
	//
	// Want to validate that if the asset type we link to has parameters and or attributes, then these parameters and attributes are part of the asset create response
	// Want to validate that the href links for asset type and tenant are valid: aka if you make a GET request with them, you get 200 response -- should be used only in specific tc's (see reasoning
	// below)
	// System generated fields are created (CreatedDate field) --- we should have a TC that covers this and not have to check all the time to avoid failing all test cases if this has an issue
	// Validate that some date fields are set to have value equal to the time of creation -- this could be validated as being the range of the tc timestamp as we cannot predict the exact second.
	// All date fields should have right format including the system generated ones -- we should do it as part of specific test cases and not check every time to avoid failing all test cases if this
	// has an issue
	//
	// Therefore the above should be made in to separate methods (possibly generalized) that can be called on demand based on test case
	//

	// RREHM-357 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithoutAttrPars() {
		requestInsight = insightRequestHelper.getInsightWithPredefinedInsightTypeAndTenant(insightTypeId, tenantId);

		responseInsight = APITestUtil.getResponseObjForCreate(requestInsight, microservice, environment, apiRequestHelper, insightAPI, InsightResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String insightId = APITestUtil.getElementId(responseInsight.get_links().getSelfLink().getHref());
		idsForAllCreatedInsights.add(insightId);

		InsightResponse committedInsight = APITestUtil.getResponseObjForRetrieve(microservice, environment, insightId, apiRequestHelper, insightAPI, InsightResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseInsight, committedInsight);
	}

	/*
	 * POSITIVE TESTS END
	 */

}
