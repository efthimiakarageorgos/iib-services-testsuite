package com.qio.assetManagement.manageAssets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MAssetAPIHelper;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.asset.AssetRequest;
import com.qio.model.asset.AssetResponse;
import com.qio.model.asset.helper.AssetRequestHelper;
import com.qio.util.common.APITestUtil;

public class CreateAssetsTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private static String assetTypeId;

	private static MTenantAPIHelper tenantAPI;
	private static String tenantId;

	private static MAssetAPIHelper assetAPI;
	private static AssetRequestHelper assetRequestHelper;
	private static AssetRequest requestAsset;
	private AssetResponse responseAsset;

	private ServerResponse serverResp;

	private static ArrayList<String> idsForAllCreatedAssets;
	private static ArrayList<String> idsForAllCreatedAssetTypes;
	private static ArrayList<String> idsForAllCreatedTenants;

	@BeforeClass
	public static void initSetupBeforeAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {
		baseInitSetupBeforeAllTests("asset");
		assetAPI = new MAssetAPIHelper();
		assetRequestHelper = new AssetRequestHelper();

		idsForAllCreatedAssets = new ArrayList<String>();
		idsForAllCreatedAssetTypes = new ArrayList<String>();
		idsForAllCreatedTenants = new ArrayList<String>();

		requestAsset = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		assetTypeId = requestAsset.getAssetType();
		tenantId = requestAsset.getTenant();

		idsForAllCreatedAssetTypes.add(assetTypeId);
		idsForAllCreatedTenants.add(tenantId);
	}

	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// Initializing a new set of objects before each test case.
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = new AssetRequest();
		responseAsset = new AssetResponse();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		baseCleanUpAfterAllTests(idsForAllCreatedAssets, assetAPI);
		// baseCleanUpAfterAllTests(idsForAllCreatedAssetTypes, assetTypeAPI); // should be un-commented once RREHM-628 starts working as expected.
		// baseCleanUpAfterAllTests(idsForAllCreatedTenants, tenantAPI); // should be un-commnented once we are able to create tenants.
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1190")) and issue in linkedIssues("RREHM-36")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-629 (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space" + origAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset  abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation Should not be Empty", serverResp);
	}

	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation is a required field.", serverResp);
	}

	// RREHM-632 (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			String origAbbr = requestAsset.getAbbreviation();
			requestAsset.setAbbreviation(specialChar + origAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name Should not Empty", serverResp);
	}

	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name is a required field.", serverResp);
	}

	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name should be less than 255 characters", serverResp);
	}

	// RREHM-638
	@Ignore
	public void shouldNotCreateAssetWhenAssetTypeDoesNotExist() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant("NonExistentAssetType", tenantId);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		// The error message here needs to be updated by Devs.
		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid tenant id in the request", serverResp);
	}

	// RREHM-636 (UPDATE)
	@Test
	public void shouldNotCreateAssetWhenTenantDoesNotExist() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, "NonExistentTenant");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		// The error message here needs to be updated by Devs.
		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid tenant id in the request", serverResp);
	}

	// RREHM-591 -- Require a bit more detail on this one.

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-628 (Asset Abbreviation contains dash, underscore, dot chars)
	@Ignore
	public void shouldCreateAssetWithUniqueAbbrWithWhenAbbrContainsDashUnderscoreDot() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation(origAbbr + "_-.");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);

		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}
	/*
	 * POSITIVE TESTS END
	 */

}
