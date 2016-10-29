/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */

package io.qio.qa.ehm.insightManagement.manageInsightActivityTypes;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.insights.MActivityTypeAPIHelper;
import io.qio.qa.lib.ehm.model.insight.activityType.ActivityType;
import io.qio.qa.lib.ehm.model.insight.activityType.helper.ActivityTypeHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;
import io.qio.qa.lib.common.MAbstractAPIHelper;

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
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(activityTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1237")) and issue in linkedIssues("RREHM-63")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-762 (ActivityType abbreviation is not unique)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrIsNotUnique() {
		String defaultAbbr = requestActivityType.getAbbreviation();

		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);
		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType requestActivityTypeNonUnique;
		requestActivityTypeNonUnique = activityTypeHelper.getActivityType();
		requestActivityTypeNonUnique.setAbbreviation(defaultAbbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityTypeNonUnique, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(409, null, "Creating Activity Type failed as another activity has same abbreviation.", serverResp);
	}

	// RREHM-768 (ActivityType abbreviation contains spaces)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpaces() {
		String abbr = requestActivityType.getAbbreviation();
		requestActivityType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab", serverResp);
	}

	// RREHM-763 (ActivityType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsLongerThan50Chars() {
		requestActivityType.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsBlank() {
		requestActivityType.setAbbreviation("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-769 (ActivityType abbreviation is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbreviationIsNull() {
		requestActivityType.setAbbreviation(null);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is mandatory, should be less than 50 characters and no special characters are allowed.", serverResp);
	}

	// RREHM-767 (ActivityType abbreviation contains special chars)
	@Test
	public void shouldNotCreateActivityTypeWhenAbbrContainsSpecialChars() {

		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestActivityType = activityTypeHelper.getActivityType();
			String origAbbr = requestActivityType.getAbbreviation();
			requestActivityType.setAbbreviation(specialChar + origAbbr);

			serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, null, "Abbreviation should not have special character except '.', '-', '_' ", serverResp);
		}
	}

	// RREHM-773 (ActivityType name is blank)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsBlank() {
		requestActivityType.setActivityName("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-773 (ActivityType Name is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsNull() {
		requestActivityType.setActivityName(null);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name is mandatory, should be less than 255 characters.", serverResp);
	}

	// RREHM-770 (ActivityType name is longer than 255 chars)
	@Test
	public void shouldNotCreateActivityTypeWhenNameIsLongerThan255Chars() {
		requestActivityType.setActivityName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Name should be less than 255 characters", serverResp);
	}

	// RREHM-777 (ActivityType Description is null - missing)
	@Test
	public void shouldNotCreateActivityTypeWhenDescIsNull() {
		requestActivityType.setActivityDescription(null);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Description is mandatory, should be of reasonable length.", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// Custom Assertion requirements - to do:
	// Need to confrim that every time we create an activity type, it gets the following element set:
	// "systemDefined" : false

	// RREHM-776 (Description has paragraphs)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWithMultiParagraphDesc() {
		requestActivityType.setActivityDescription("A Desc \r\n new line \n\r new line");
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-771 (Name contains special chars)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameContainsSpecialChars() {
		requestActivityType.setActivityName(APITestUtil.SPECIAL_CHARS);
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-764 (Name contains special chars)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenAbbrContainsDotDashUnderscore() {
		String defaultAbbr = requestActivityType.getAbbreviation();
		requestActivityType.setAbbreviation(".-_" + defaultAbbr);
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-1874 (Name is 255 chars long)
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameIs255CharsLong() {

		requestActivityType.setActivityName(APITestUtil.TWOFIFTYFIVE_CHARS);
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-1875
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenAbbrIs50CharsLong() {

		String defaultAbbr = requestActivityType.getAbbreviation();

		String fiftyChars = APITestUtil.FIFTY_CHARS;
		String partialFiftyChars = fiftyChars.substring(defaultAbbr.length());

		requestActivityType.setAbbreviation(defaultAbbr + partialFiftyChars);
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	
	// RREHM-775
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenDescContainsSpecialChars() {

		requestActivityType.setActivityDescription(APITestUtil.SPECIAL_CHARS);
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-772
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbrWhenNameIsNotUnique() {

		ActivityType responseActivityTypeNonUniqueName;
		ActivityType requestActivityTypeNonUniqueName;

		String defaultName = requestActivityType.getActivityName();
		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);
		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		requestActivityTypeNonUniqueName = activityTypeHelper.getActivityType();
		requestActivityTypeNonUniqueName.setActivityName(defaultName);

		responseActivityTypeNonUniqueName = MAbstractAPIHelper.getResponseObjForCreate(requestActivityTypeNonUniqueName, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeNonUniqueNameId = responseActivityTypeNonUniqueName.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeNonUniqueNameId);

		ActivityType committedActivityType = APITestUtil.getResponseObjForRetrieve(microservice, environment, activityTypeNonUniqueNameId, apiRequestHelper, activityTypeAPI, ActivityType.class);

		CustomAssertions.assertRequestAndResponseObj(responseActivityTypeNonUniqueName, committedActivityType);
	}

	// RREHM-761
	@Test
	public void shouldCreateActivityTypeWithUniqueAbbr() {

		responseActivityType = MAbstractAPIHelper.getResponseObjForCreate(requestActivityType, microservice, environment, apiRequestHelper, activityTypeAPI, ActivityType.class);

		String activityTypeId = responseActivityType.getActivityTypeId();
		idsForAllCreatedElements.add(activityTypeId);

		ActivityType committedActivityType = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, activityTypeId, apiRequestHelper, activityTypeAPI, ActivityType.class);
		CustomAssertions.assertRequestAndResponseObj(responseActivityType, committedActivityType);
	}

	// RREHM-774
	// RREHM-765 --> This is a delete test case. Currently no deletes area allowed at all. Should be used in the future.

	/*
	 * POSITIVE TESTS END
	 */
}
