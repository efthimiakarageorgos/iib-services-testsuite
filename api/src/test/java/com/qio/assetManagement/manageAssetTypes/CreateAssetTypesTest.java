
package com.qio.assetManagement.manageAssetTypes;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.util.common.APITestUtil;

public class CreateAssetTypesTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1189")) and issue in linkedIssues("RREHM-41")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-383 (AssetType abbreviation is not unique)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrIsNotUnique() {
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);

		AssetType requestAssetTypeWithSameAbbr = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		requestAssetTypeWithSameAbbr.setAbbreviation(requestAssetType.getAbbreviation());
		serverResp = APITestUtil.getResponseObjForCreate(requestAssetTypeWithSameAbbr, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "org.springframework.dao.DuplicateKeyException", serverResp);
	}

	// RREHM-435 (AssetType abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrContainsSpaces() {
		// requestAssetType = assetTypeHelper.getAssetTypeWithDefaultParameter();

		String abbr = requestAssetType.getAbbreviation();
		requestAssetType.setAbbreviation("Abrr has a space" + abbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-436 (AssetType abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsLongerThan50Chars() {
		requestAssetType.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-468 (AssetType abbreviation is blank)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsBlank() {
		requestAssetType.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset type abbreviation is a required field", serverResp);
	}

	// RREHM-385 (AssetType abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbreviationIsNull() {
		requestAssetType.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset type abbreviation is a required field", serverResp);
	}

	// RREHM-433 (AssetType abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAbbrContainsSpecialChars() {
		String defaultAbbr = requestAssetType.getAbbreviation();
		int count = APITestUtil.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestAssetType.setAbbreviation(APITestUtil.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-438 (AssetType name is blank)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsBlank() {
		requestAssetType.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset type name is a required field", serverResp);
	}

	// RREHM-384 (AssetType Name is null - missing)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsNull() {
		requestAssetType.setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset type name is a required field", serverResp);
	}

	// RREHM-437 (AssetType name is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenNameIsLongerThan50Chars() {
		requestAssetType.setName(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Name should be less than 50 characters", serverResp);
	}

	// RREHM-440 (AssetType description is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetTypeWhenDescriptionIsLongerThan255Chars() {
		requestAssetType.setDescription(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Type Description should be less than 255 characters", serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-380 ()

	// RREHM-382 ()

	// RREHM-431 ()

	// RREHM-432 ()

	// RREHM-439 ()

	// RREHM-454 ()

	// RREHM-466 ()

	// RREHM-482 (not automatable)
}
