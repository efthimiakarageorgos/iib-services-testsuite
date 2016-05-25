
package com.qio.insightManagement.manageInsightActivityTypes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.insights.MActivityTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.insight.activityType.ActivityType;
import com.qio.model.insight.activityType.helper.ActivityTypeHelper;
import com.qio.util.common.APITestUtil;

public class CreateActivityTypesTest extends BaseTestSetupAndTearDown {

	private static MActivityTypeAPIHelper activityTypeAPI;
	private ActivityTypeHelper activityTypeHelper;
	private ActivityType requestActivityType;
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
		requestActivityType = activityTypeHelper.getActivityType();
		responseActivityType = new ActivityType();
		serverResp = new ServerResponse();
	}

	// Currently the deletion of activity types is not allowed; therefore we are commenting out this method.
	 @AfterClass
	 public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException,
	 	IllegalArgumentException,InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		 baseCleanUpAfterAllTests(activityTypeAPI);
	 }

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1237")) and issue in linkedIssues("RREHM-63")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-762 (ActivityType abbreviation is not unique)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String defaultAbbr = requestActivityType.getAbbreviation();

		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);
		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);
		ActivityType requestActivityTypeNonUnique;
		requestActivityTypeNonUnique = activityTypeHelper.getActivityType();
		requestActivityTypeNonUnique.setAbbreviation(defaultAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityTypeNonUnique, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(409, null, "Creating Activity Type failed as another activity has same abbreviation.", serverResp);
	}

	// RREHM-768 (ActivityType abbreviation contains spaces)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String abbr = requestActivityType.getAbbreviation();
		requestActivityType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab", serverResp);
	}

	// RREHM-763 (ActivityType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-767 (ActivityType abbreviation contains special chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		int count = APITestUtil.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestActivityType = activityTypeHelper.getActivityType();
			String defaultAbbr = requestActivityType.getAbbreviation();
			requestActivityType.setAbbreviation(APITestUtil.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, null, "Abbreviation should not have special character except '.', '-', '_' ", serverResp);
		}
	}

	// RREHM-773 (ActivityType name is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-773 (ActivityType Name is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-770 (ActivityType name is longer than 255 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name should be less than 255 characters", serverResp);
	}

	// RREHM-777 (ActivityType Description is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenDescIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityDescription(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Description is mandatory, should be of reasonable length.", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// Custom Assertion requirements - to do:
	// Need to confirm that every time we create an activity type, it gets the following element set:
	// "systemDefined" : false

	// RREHM-776 (Description has paragraphs)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWithMultiParagraphDesc() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityDescription("A Desc \r\n new line \n\r new line");
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-771 (Name contains special chars)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestActivityType.setActivityName(APITestUtil.SPECIAL_CHARS);
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-764 (Name contains special chars)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenAbbrContainsDotDashUnderscore() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String defaultAbbr = requestActivityType.getAbbreviation();
		requestActivityType.setAbbreviation(".-_" + defaultAbbr);
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-1874 (Name is 255 chars long)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameIs255CharsLong() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		requestActivityType.setActivityName(APITestUtil.TWOFIFTYFIVE_CHARS);
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-1875
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenAbbrIs50CharsLong() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		String defaultAbbr = requestActivityType.getAbbreviation();

		String fiftyChars = APITestUtil.FIFTY_CHARS;
		String partialFiftyChars = fiftyChars.substring(defaultAbbr.length());

		requestActivityType.setAbbreviation(defaultAbbr + partialFiftyChars);
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-775
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenDescContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		requestActivityType.setActivityDescription(APITestUtil.SPECIAL_CHARS);
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-772
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		ActivityType responseActivityTypeNonUniqueName;
		ActivityType requestActivityTypeNonUniqueName;

		String defaultName = requestActivityType.getActivityName();
		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);
		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		requestActivityTypeNonUniqueName = activityTypeHelper.getActivityType();
		requestActivityTypeNonUniqueName.setActivityName(defaultName);

		responseActivityTypeNonUniqueName = APITestUtil.getResponseObjForCreate(requestActivityTypeNonUniqueName, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeNonUniqueNameId = responseActivityTypeNonUniqueName.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeNonUniqueNameId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeNonUniqueNameId, apiRequestHelper, activityTypeAPI, ActivityType.class);

		CustomAssertions.assertRequestAndResponseObj(responseActivityTypeNonUniqueName, committedActivityType);
	}

	// RREHM-761
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbr() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		responseActivityType = APITestUtil.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-774
	// RREHM-765 --> This is a delete test case. Currently no deletes area allowed at all. Should be used in the future.

	/*
	 * POSITIVE TESTS END
	 */
}
