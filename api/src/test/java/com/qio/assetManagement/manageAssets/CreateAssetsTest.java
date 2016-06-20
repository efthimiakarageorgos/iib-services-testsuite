package com.qio.assetManagement.manageAssets;

import java.util.ArrayList;

import org.apache.log4j.Logger;
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
import com.qio.model.asset.AssetStatus;
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
	private static AssetResponse responseAsset;

	private ServerResponse serverResp;

	private static ArrayList<String> idsForAllCreatedAssets;
	private static ArrayList<String> idsForAllCreatedAssetTypes;
	private static ArrayList<String> idsForAllCreatedTenants;

	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetAPI = new MAssetAPIHelper();
		assetRequestHelper = new AssetRequestHelper();

		assetTypeAPI = new MAssetTypeAPIHelper();
		tenantAPI = new MTenantAPIHelper();

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
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = new AssetRequest();
		responseAsset = new AssetResponse();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(idsForAllCreatedAssets, assetAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedAssetTypes, assetTypeAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedTenants, tenantAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1190")) and issue in linkedIssues("RREHM-36")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-629 (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space" + origAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset  abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation Should not be Empty", serverResp);
	}

	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation is a required field.", serverResp);
	}

	// RREHM-632 (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() {
		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			String origAbbr = requestAsset.getAbbreviation();
			requestAsset.setAbbreviation(specialChar + origAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name Should not Empty", serverResp);
	}

	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name is a required field.", serverResp);
	}

	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name should be less than 255 characters", serverResp);
	}

	// RREHM-638
	@Test
	public void shouldNotCreateAssetWhenAssetTypeDoesNotExist() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant("NonExistentAssetType", tenantId);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Invalid Asset Type ID", serverResp);
	}

	// RREHM-636
	@Test
	public void shouldNotCreateAssetWhenTenantDoesNotExist() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, "NonExistentTenant");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid tenant id in the request", serverResp);
	}

	// RREHM-591
	@Test
	public void shouldNotCreateAssetWhenAbbrIsNotUniqueInTenantScope() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);

		AssetRequest requestAssetNonUnique = new AssetRequest(requestAsset); // assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		// requestAssetNonUnique.setAbbreviation(origAbbr);
		serverResp = APITestUtil.getResponseObjForCreate(requestAssetNonUnique, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Abbreviation is not unique.", serverResp);

		// TODO: Jeet
		// Confirm that there is only one asset under the tenant by submitting GET with URL:
		// {Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid=tenantId
	}

	// RREHM-623
	@Test
	public void shouldNotCreateAssetWhenStatusIsNotValid() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus("NotValid");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Invalid asset status", serverResp);
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
	// Validate that some date fields are set to have value equal to the time of creation -- this could be validated as being the range of the tc timestamp as we cannot predict the exact second. -- Not sure how to do this?
	// Therefore the above should be made in to separate methods (possibly generalized) that can be called on demand based on test case
	//

	// RREHM-357 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithoutAttrPars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-778 ()
	@Test
	public void shouldHaveCreatedDateFieldGeneratedbySystemWhenCreatingAsset() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		CustomAssertions.assertDateFormat(responseAsset.getCreatedDate()); // Not sure how can we check the time of creation for equality?
	}

	// RREHM-628 (Asset Abbreviation contains dash, underscore, dot chars)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenAbbrContainsDashUnderscoreDot() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation(origAbbr + "_-.");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-660 (Asset Description contains paragraphs)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenDescContainsParagraphs() {

		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setDescription("This is paragraph 1.\n This is paragraph 2.\nThis is paragraph 3");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-779 ()
	// BUG: RREHM-956
	@Ignore
	public void shouldCreateAssetWithNonUniqueAbbrWhenUniqueInTenantScope() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		// This is a trick to get a second tenant created and after that create another asset that has the same abbreviation as the previously created asset and tenant set to the tenant of this asset
		AssetRequest requestAssetForSecondTenantWithSameAbbr = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		idsForAllCreatedAssetTypes.add(requestAssetForSecondTenantWithSameAbbr.getAssetType());
		idsForAllCreatedTenants.add(requestAssetForSecondTenantWithSameAbbr.getTenant());
		requestAssetForSecondTenantWithSameAbbr.setAbbreviation(origAbbr);

		AssetResponse responseAssetForSecondTenantWithSameAbbr = APITestUtil.getResponseObjForCreate(requestAssetForSecondTenantWithSameAbbr, microservice, environment, apiRequestHelper, assetAPI,
				AssetResponse.class);
		idsForAllCreatedAssets.add(APITestUtil.getElementId(responseAssetForSecondTenantWithSameAbbr.get_links().getSelfLink().getHref()));

		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-823
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenStatusIsValid() {

		for (AssetStatus eachAssetStatus : AssetStatus.values()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			requestAsset.setStatus(eachAssetStatus.toString());
			responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
			String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
			idsForAllCreatedAssets.add(assetId);

			AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
			CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		}
	}

	// RREHM-824 ()
	@Test
	public void shouldCreateAssetWhenStatusIsNullAndDefaultStatusValue() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus(null);

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		CustomAssertions.assertEqualityCheckOnInputFields(AssetStatus.CREATED.toString(), committedAsset.getStatus());
	}

	// RREHM-824 ()
	@Test
	public void shouldCreateAssetWhenStatusIsEmptyAndDefaultStatusValue() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus("");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		CustomAssertions.assertEqualityCheckOnInputFields(AssetStatus.CREATED.toString(), committedAsset.getStatus());
	}

	// RREHM-612
	// RREHM-610
	// RREHM-609
	// RREHM-621

	/*
	 * POSITIVE TESTS END
	 */

}
