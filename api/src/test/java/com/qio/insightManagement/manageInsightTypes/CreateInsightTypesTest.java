
package com.qio.insightManagement.manageInsightTypes;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.insights.MInsightTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.insight.insightType.InsightType;
import com.qio.model.insight.insightType.helper.InsightTypeHelper;
import com.qio.util.common.APITestUtil;

public class CreateInsightTypesTest extends BaseTestSetupAndTearDown {

	private static MInsightTypeAPIHelper insightTypeAPI;
	private InsightTypeHelper insightTypeHelper;
	private InsightType requestInsightType;
	private InsightType responseInsightType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("insight");
		insightTypeAPI = new MInsightTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		// Initializing a new set of objects before each test case.
		insightTypeHelper = new InsightTypeHelper();
		requestInsightType = insightTypeHelper.getInsightTypeWithNoAttributes();
		responseInsightType = new InsightType();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(insightTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1235")) and issue in linkedIssues("RREHM-XXX")
	// None of the test are marked as automated as they need to be cleaned up

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-511 (NEED feedback from Kunaal on validity of test case)

	// RREHM-516 (InsightType has non unique abbreviation)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbrIsNotUnique() {
		responseInsightType = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, InsightType.class);
		String insightTypeId = APITestUtil.getElementId(responseInsightType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(insightTypeId);

		InsightType requestInsightTypeWithSameAbbr = insightTypeHelper.getInsightTypeWithNoAttributes();
		requestInsightTypeWithSameAbbr.setAbbreviation(requestInsightType.getAbbreviation());
		serverResp = APITestUtil.getResponseObjForCreate(requestInsightTypeWithSameAbbr, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(409, null, "Insight type creation failed as another insight type has same abbreviation.", serverResp);
	}

	// RREHM-519 (InsightType abbreviation contains spaces)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbrContainsSpaces() {
		String abbr = requestInsightType.getAbbreviation();
		requestInsightType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab", serverResp);
	}

	// RREHM-517 (InsightType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsLongerThan50Chars() {
		requestInsightType.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters", serverResp);
	}

	// RREHM-788 (InsightType abbreviation is blank)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsBlank() {
		requestInsightType.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-788 (InsightType abbreviation is null - missing)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsNull() {
		requestInsightType.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-xxx (InsightType abbreviation contains special chars)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbrContainsSpecialChars() {
		String defaultAbbr = requestInsightType.getAbbreviation();
		int count = APITestUtil.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestInsightType.setAbbreviation(APITestUtil.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, null, "Abbreviation should not have special character except '.', '-', '_' ", serverResp);
		}
	}

	// RREHM-789 (InsightType name is blank)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsBlank() {
		requestInsightType.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name is required, should be less than 255 char", serverResp);
	}

	// RREHM-789 (InsightType Name is null - missing)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsNull() {
		requestInsightType.setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name is required, should be less than 255 char", serverResp);
	}

	// RREHM-790 (InsightType description is blank)
	@Test
	public void shouldNotCreateInsightTypeWhenDescriptionIsBlank() {
		requestInsightType.setDescription("");

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Description is mandatory, should be of reasonable length.", serverResp);
	}

	// RREHM-521 (InsightType name is longer than 255 chars)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsLongerThan50Chars() {
		requestInsightType.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name should be less than 255 characters", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-522
	// RREHM-523
	// RREHM-524
	// RREHM-520
	// RREHM-518
	// RREHM-515
	// RREHM-510
}
