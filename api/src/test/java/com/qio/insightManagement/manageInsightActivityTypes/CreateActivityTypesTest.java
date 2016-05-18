
package com.qio.insightManagement.manageInsightActivityTypes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.insights.MActivityTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.insight.activityType.ActivityType;
import com.qio.model.insight.activityType.helper.ActivityTypeHelper;
import com.qio.testHelper.TestHelper;

public class CreateActivityTypesTest extends BaseTestSetupAndTearDown {

	private static MActivityTypeAPIHelper activityTypeAPI;
	private ActivityTypeHelper activityTypeHelper;
	private ActivityType requestActivityType;
	private ActivityType requestActivityType2;
	private ActivityType responseActivityType;
	private ServerResponse serverResp;

	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("insight");
		activityTypeAPI = new MActivityTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		activityTypeHelper = new ActivityTypeHelper();
		requestActivityType = new ActivityType();
		responseActivityType = new ActivityType();
		serverResp = new ServerResponse();

		requestActivityType = activityTypeHelper.getActivityType();
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1237")) and issue in linkedIssues("RREHM-63")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-762 (ActivityType abbreviation is not unique)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String abbr = requestActivityType.getAbbreviation();

		// WIP --- Need Jeets' help -- this request fails
		responseActivityType = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ActivityType.class);

		requestActivityType2 = new ActivityType();
		requestActivityType2.setAbbreviation(abbr);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType2, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);
		CustomAssertions.assertServerError(400, null,
				"Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-768 (ActivityType abbreviation contains spaces)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String abbr = requestActivityType.getAbbreviation();
		requestActivityType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab", serverResp);
	}

	// RREHM-763 (ActivityType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation(TestHelper.FIFTYONE_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation("");

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null,
				"Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation(null);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null,
				"Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-767 (ActivityType abbreviation contains special chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String defaultAbbr = requestActivityType.getAbbreviation();
		int count = TestHelper.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestActivityType.setAbbreviation(TestHelper.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
					ServerResponse.class);

			CustomAssertions.assertServerError(400, null, "Abbreviation should not have special character except '.', '-', '_' ", serverResp);
		}
	}

	// RREHM-773 (ActivityType name is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName("");

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-773 (ActivityType Name is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName(null);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-770 (ActivityType name is longer than 255 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName(TestHelper.TWOFIFTYSIX_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name should be less than 255 characters", serverResp);
	}

	// RREHM-777 (ActivityType Description is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenDescIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityDescription(null);

		serverResp = TestHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI,
				ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Description is mandatory, should be of reasonable length.", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1874
	// RREHM-1875
	// RREHM-892
	// RREHM-776
	// RREHM-775
	// RREHM-774
	// RREHM-772
	// RREHM-771
	// RREHM-764
	// RREHM-765
	// RREHM-766
	// RREHM-761
	/*
	 * POSITIVE TESTS END
	 */
}
