/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package com.hbc.qa.iib.assetManagement.manageAssets;

import java.util.ArrayList;

import com.hbc.qa.lib.iib.common.TenantUtil;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import com.hbc.qa.iib.common.BaseTestSetupAndTearDown;
import com.hbc.qa.lib.iib.common.APITestUtil;

import com.hbc.qa.lib.iib.model.asset.AssetRequest;
import com.hbc.qa.lib.iib.model.asset.AssetResponse;
import com.hbc.qa.lib.iib.model.asset.AssetStatus;
import com.hbc.qa.lib.iib.model.asset.helper.AssetRequestHelper;
import com.hbc.qa.lib.iib.apiHelpers.asset.MAssetAPIHelper;
import com.hbc.qa.lib.iib.apiHelpers.tenant.MTenantAPIHelper;
import com.hbc.qa.lib.iib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.hbc.qa.lib.iib.model.assetType.AssetType;

import com.hbc.qa.lib.assertions.CustomAssertions;
import com.hbc.qa.lib.exception.ServerResponse;
import com.hbc.qa.lib.common.MAbstractAPIHelper;


public class CreateAssetsTest extends BaseTestSetupAndTearDown {
	private static MAssetTypeAPIHelper assetTypeAPI;
	private static String assetTypeId;
	private static String assetTypeMPId;
	private static String assetTypeMAId;

	private static MTenantAPIHelper tenantAPI;
	private static String tenantId;
	private static String tenantMPId;
	private static String tenantMAId;

	private static MAssetAPIHelper assetAPI;
	private static AssetRequestHelper assetRequestHelper;
	private static AssetRequest requestAsset;
	private static AssetResponse responseAsset;

	private ServerResponse serverResp;

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

		idsForAllCreatedAssetTypes = new ArrayList<>();
		idsForAllCreatedTenants = new ArrayList<>();

		requestAsset = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		assetTypeId = requestAsset.getAssetType();
		tenantId = requestAsset.getTenant();

		idsForAllCreatedAssetTypes.add(assetTypeId);
		idsForAllCreatedTenants.add(tenantId);

		AssetRequest requestAssetManyPars = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithAllParameters", null, null);
		assetTypeMPId = requestAssetManyPars.getAssetType();
		tenantMPId = requestAssetManyPars.getTenant();

		idsForAllCreatedAssetTypes.add(assetTypeMPId);
		idsForAllCreatedTenants.add(tenantMPId);

		AssetRequest requestAssetManyAttrs = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithAllAttributes", null, null);
		assetTypeMAId = requestAssetManyAttrs.getAssetType();
		tenantMAId = requestAssetManyAttrs.getTenant();

		idsForAllCreatedAssetTypes.add(assetTypeMAId);
		idsForAllCreatedTenants.add(tenantMAId);
	}

	@Before
	public void initSetupBeforeEveryTest() {
		// Initializing a new set of objects before each test case.
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = new AssetRequest();
		responseAsset = new AssetResponse();
		serverResp = new ServerResponse();
		
		username = userConfig.getString("user.superuser.username");
		password = userConfig.getString("user.superuser.password");
		apiRequestHelper.setUserName(username);
		apiRequestHelper.setPassword(password);
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedAssetTypes, assetTypeAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedTenants, tenantAPI);

		// INFO: This delete will not be required if we move to MVP3 code
		ArrayList<String> idsForAllCreatedGroupsForTenants;
		idsForAllCreatedGroupsForTenants = new ArrayList<>();

		for (String elementId : idsForAllCreatedTenants) {
			TenantUtil tenantUtil;
			tenantUtil = new TenantUtil();
			String groupId = tenantUtil.getIDMGroupForTenant(elementId);
			//logger.info("Adding group id to list "+ groupId);
			idsForAllCreatedGroupsForTenants.add(groupId);
		}
		baseCleanUpAfterAllTests(idsForAllCreatedGroupsForTenants, groupAPI, oauthMicroserviceName);
	}

	// Matching test cases in Test Case Management (Jira/Zephyr):
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

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset  abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation Should not be Empty", serverResp);
	}

	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(null);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation is a required field.", serverResp);
	}

	// RREHM-632 (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() {
		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			String origAbbr = requestAsset.getAbbreviation();
			requestAsset.setAbbreviation(specialChar + origAbbr);

			serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

			CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name Should not Empty", serverResp);
	}

	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(null);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name is a required field.", serverResp);
	}

	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Asset name should be less than 255 characters", serverResp);
	}

	// RREHM-638
	@Test
	public void shouldNotCreateAssetWhenAssetTypeDoesNotExist() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant("NonExistentAssetType", tenantId);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Invalid Asset Type ID", serverResp);
	}

	// RREHM-636
	@Test
	public void shouldNotCreateAssetWhenTenantDoesNotExist() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, "NonExistentTenant");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid tenant id in the request", serverResp);
	}

	// RREHM-591
	@Test
	public void shouldNotCreateAssetWhenAbbrIsNotUniqueInTenantScope() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);

		AssetRequest requestAssetNonUnique = new AssetRequest(requestAsset); // assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		// requestAssetNonUnique.setAbbreviation(origAbbr);
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAssetNonUnique, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Abbreviation is not unique.", serverResp);

		// TODO
		// Confirm that there is only one asset under the tenant by submitting GET with URL:
		// {Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid=tenantId
	}

	// RREHM-623
	@Test
	public void shouldNotCreateAssetWhenStatusIsNotValid() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus("NotValid");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Invalid asset status", serverResp);
	}

	// RREHM-XXX (User does not belong to neither SUPERUSER nor ADMIN groups)
	@Test
	public void shouldNotCreateAssetWhenUserDoesNotBelongToNeitherSuperuserNorAdminGroup() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		username = userConfig.getString("user.user.username");
		password = userConfig.getString("user.user.password");
		apiRequestHelper.setUserName(username);
		apiRequestHelper.setPassword(password);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(403, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	// TODO
	// MORE ASSERTIONS REQS for NEGATIVE:
	// We could use one tenant and asset type for negative tests and a separate pair for positive
	// In that case we could also validate that the tenant used for the positive tests does not have any assets associated with it
	// at the end of each negative test by sending the GET request:
	// {Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid={tenantId}

	/*
	 * POSITIVE TESTS START
	 */

	// TODO
	// MORE ASSERTIONS REQS for POSITIVE:
	//
	// Want to validate that if the asset type we link to has parameters and or attributes, then these parameters and attributes are part of the asset create response
	// Want to validate that the href links for asset type and tenant are valid: aka if you make a GET request with them, you get 200 response -- should be used only in specific tc's (see reasoning
	// below)
	// Validate that some date fields are set to have value equal to the time of creation -- this could be validated as being the range of the tc timestamp as we cannot predict the exact second.
	//
	// Therefore the above should be made in to separate methods (possibly generalized) that can be called on demand based on test case
	//

	// RREHM-357 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithoutAttrPars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}


	// RREHM-778 ()
	@Test
	public void shouldHaveCreatedDateFieldGeneratedbySystemWhenCreatingAsset() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);

		// Assert the CreatedDate has the right date format
		Assert.assertTrue(responseAsset.validateDateFormats());
		Assert.assertTrue(committedAsset.validateDateFormats());

	    // TODO
		// Assert the CreatedDate field value is equal to the time of creation
	}

	// RREHM-628 (Asset Abbreviation contains dash, underscore, dot chars)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenAbbrContainsDashUnderscoreDot() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation(origAbbr + "_-.");

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset );

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-660 (Asset Description contains paragraphs)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenDescContainsParagraphs() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setDescription("This is paragraph 1.\n This is paragraph 2.\nThis is paragraph 3");

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset );

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}

	// RREHM-779 ()
	// BUG: RREHM-956
	@Ignore
	public void shouldCreateAssetWithNonUniqueAbbrWhenUniqueInTenantScope() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		idsForAllCreatedElements.add(responseAsset.getAssetId());

		// Create another asset that has the same abbreviation as the previously created asset and different tenant
		AssetRequest requestAssetForSecondTenantWithSameAbbr = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantMPId);
		requestAssetForSecondTenantWithSameAbbr.setAbbreviation(origAbbr);

		AssetResponse responseAssetForSecondTenantWithSameAbbr = MAbstractAPIHelper.getResponseObjForCreate(requestAssetForSecondTenantWithSameAbbr, microservice, environment, apiRequestHelper, assetAPI,
				AssetResponse.class);
		String assetId = responseAssetForSecondTenantWithSameAbbr.getAssetId();
		idsForAllCreatedElements.add(assetId);

		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAssetForSecondTenantWithSameAbbr, responseAssetForSecondTenantWithSameAbbr);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetForSecondTenantWithSameAbbr, committedAsset);
	}

	// RREHM-823
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenStatusIsValid() {

		for (AssetStatus eachAssetStatus : AssetStatus.values()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			requestAsset.setStatus(eachAssetStatus.toString());

			responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
			String assetId = responseAsset.getAssetId();
			idsForAllCreatedElements.add(assetId);

			//Assert response code and equality of request and response
			CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

			AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
			CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		}
	}

	// RREHM-824 ()
	@Test
	public void shouldCreateAssetWhenStatusIsNullAndDefaultStatusValue() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus(null);

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertEqualityCheckOnInputFields("AssetCreated", committedAsset.getStatus());
	}
	
	// RREHM-824 ()
	@Test
	public void shouldCreateAssetWhenStatusIsEmptyAndDefaultStatusValue() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus("");
		
		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertEqualityCheckOnInputFields("AssetCreated", committedAsset.getStatus());
		//above is same as the line below, not sure if it is worth to define the method used above
		//assertEquals("AssetCreated", committedAsset.getStatus());
	}


	// RREHM-612 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithMultiplePars() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeMPId, tenantMPId);

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);

		AssetType linkedAssetType = committedAsset.getAssetType();
		assertTrue (linkedAssetType.getParameters().size() != 0);
	}

	// RREHM-621 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithMultipleAttrs() {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeMAId, tenantMAId);

		responseAsset = MAbstractAPIHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = responseAsset.getAssetId();
		idsForAllCreatedElements.add(assetId);

		//Assert response code and equality of request and response
		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, requestAsset, responseAsset);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);

		AssetType linkedAssetType = committedAsset.getAssetType();
		assertTrue (linkedAssetType.getAttributes().size() != 0);
	}

	// RREHM-610
	// RREHM-609
	
	/*
	 * POSITIVE TESTS END
	 */

}
