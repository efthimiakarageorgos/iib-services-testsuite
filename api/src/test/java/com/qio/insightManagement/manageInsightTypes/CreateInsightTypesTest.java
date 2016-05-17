
package com.qio.insightManagement.manageInsightTypes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.insights.MInsightTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.insightType.InsightType;
import com.qio.model.insightType.helper.InsightTypeHelper;
import com.qio.testHelper.TestHelper;

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
		requestInsightType = new InsightType();
		responseInsightType = new InsightType();
		serverResp = new ServerResponse();

		requestInsightType = insightTypeHelper.getAssetTypeWithNoAttributes();
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1235")) and issue in linkedIssues("RREHM-41")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-XXX (InsightType abbreviation contains spaces)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String abbr = requestInsightType.getAbbreviation();
		requestInsightType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab", serverResp);
	}

	// RREHM-517 (InsightType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setAbbreviation(TestHelper.FIFTYONE_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters", serverResp);
	}

	// RREHM-788 (InsightType abbreviation is blank)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setAbbreviation("");

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null,
				"Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-788 (InsightType abbreviation is null - missing)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setAbbreviation(null);

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null,
				"Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-xxx (InsightType abbreviation contains special chars)
	@Test
	public void shouldNotCreateInsightTypeWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String defaultAbbr = requestInsightType.getAbbreviation();
		int count = TestHelper.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestInsightType.setAbbreviation(TestHelper.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
					ServerResponse.class);

			CustomAssertions.assertServerError(400, null, "Abbreviation should not have special character except '.', '-', '_' ", serverResp);
		}
	}

	// RREHM-789 (InsightType name is blank)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setName("");

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name is required, should be less than 255 char", serverResp);
	}

	// RREHM-789 (InsightType Name is null - missing)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setName(null);

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name is required, should be less than 255 char", serverResp);
	}

	// RREHM-521 (InsightType name is longer than 255 chars)
	@Test
	public void shouldNotCreateInsightTypeWhenNameIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestInsightType.setName(TestHelper.TWOFIFTYSIX_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestInsightType, microservice, environment, apiRequestHelper, insightTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Insight name should be less than 255 characters", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-522
}
