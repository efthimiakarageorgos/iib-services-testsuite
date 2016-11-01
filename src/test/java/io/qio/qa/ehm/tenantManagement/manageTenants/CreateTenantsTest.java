/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.tenantManagement.manageTenants;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.MTenantAPIHelper;
import io.qio.qa.lib.idm.apiHelpers.MUserGroupAPIHelper;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;
import io.qio.qa.lib.ehm.model.tenant.Tenant;
import io.qio.qa.lib.ehm.model.tenant.helper.TenantHelper;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.ehm.common.TenantUtil;
import io.qio.qa.lib.common.MAbstractAPIHelper;

public class CreateTenantsTest extends BaseTestSetupAndTearDown {

    private static MTenantAPIHelper tenantAPI;
    private static MUserGroupAPIHelper groupAPI;
    private static TenantUtil tenantUtil;
    private TenantHelper tenantHelper;
    private Tenant requestTenant;
    private Tenant responseTenant;
    private Tenant requestTenant2;
    private ServerResponse serverResp;

    final static Logger logger = Logger.getRootLogger();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
        baseInitSetupBeforeAllTests("tenant");

        //INFO: Only users in the ADMIN group can create a tenant
        username = userConfig.getString("user.admin.username");
        password = userConfig.getString("user.admin.password");
        apiRequestHelper.setUserName(username);
        apiRequestHelper.setPassword(password);

        tenantAPI = new MTenantAPIHelper();
        groupAPI = new MUserGroupAPIHelper();

        tenantUtil = new TenantUtil();
    }

    @Before
    public void initSetupBeforeEveryTest() {
        tenantHelper = new TenantHelper();
        requestTenant = tenantHelper.getTenant();
        responseTenant = new Tenant();
        serverResp = new ServerResponse();
    }

    @AfterClass
    public static void cleanUpAfterAllTests() {
        // INFO:Currently no user is allowed to delete a tenant
        baseCleanUpAfterAllTests(tenantAPI);

        // INFO: This delete will not be required if we move to MVP3 code
        for (String elementId : idsForAllCreatedElements) {
            String groupId = tenantUtil.getIDMGroupForTenant(elementId);
            //logger.info("Adding group id to list "+ groupId);
            secondaryIdListForDeletion.add(groupId);
        }
        baseCleanUpAfterAllTests(secondaryIdListForDeletion, groupAPI, "idm");
    }

	// Matching test cases in Test Case Management (Jira/Zephyr):
	// issuetype=Test and issue in (linkedIssues("RREHM-1191")) and issue in linkedIssues("RREHM-37")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-338 (Tenant with no unique abbreviation)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() {
		requestTenant2 = tenantHelper.getTenant();
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant2, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);
		String tenantAbbr2 = requestTenant2.getAbbreviation();

		// Setting Tenant abbreviation to be the same as the name of tenant2
		requestTenant.setAbbreviation(tenantAbbr2);
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(409, null, "Creating tenant failed, as another tenant has same abbreviation.", serverResp);
		// GetTenant should not return two tenants with the same abbreviation
		
	}

	// RREHM-352 (Tenant abbreviation contains spaces)
	@Test
	public void shouldNotCreateTenantWhenAbbrContainsSpaces() {
		String defaultAbbr = requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("Abbr has spaces" + defaultAbbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab.", serverResp);
		// GetTenant should not return a tenant with Abbreviation as specified above
	}

	// RREHM-334 (Tenant abbreviation is null - missing)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNull() {
		String defaultAbbr = requestTenant.getAbbreviation();

		// Setting Tenant abbreviation to null
		requestTenant.setAbbreviation(null);
		requestTenant.setName("ThisTenantShouldNotGetCreated" + defaultAbbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is required, should be less than 50 characters.", serverResp);
		// GetTenant should not return a tenant with Name as specified above
		// No IDM group should be created
	}

	// RREHM-334 (Tenant abbreviation is empty)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsEmpty() {

		String defaultAbbr = requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("");
		requestTenant.setName("ThisTenantShouldNotGetCreated" + defaultAbbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation is required, should be less than 50 characters.", serverResp);
		// GetTenant should not return a tenant with Name as specified above.
		// No IDM group should be created
	}

	// RREHM-984 (Tenant abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateTenantWhenAbbrIsLongerThan50Chars() {

		String defaultAbbr = requestTenant.getAbbreviation();
		requestTenant.setAbbreviation(APITestUtil.FIFTYONE_CHARS);
		requestTenant.setName("ThisTenantShouldNotGetCreated" + defaultAbbr);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters.", serverResp);
		// GetTenant should not return a tenant with abbr as specified above.
		// No IDM group should be created
	}

	// RREHM-833 (Tenant name is blank)
	@Test
	public void shouldNotCreateTenantWhenNameIsBlank() {

		requestTenant.setName("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name is required, should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to ""
	}

	// RREHM-833 (Tenant Name is null - missing)
	@Test
	public void shouldNotCreateTenantWhenNameIsNull() {

		requestTenant.setName("");

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name is required, should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to null

	}

	// RREHM-985 (Tenant name is longer than 255 chars)
	@Test
	public void shouldNotCreateTenantWhenNameIsLongerThan255Chars() {

		requestTenant.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to name specified above
	}
	/*
	 * NEGATIVE TESTS END
	 */
	

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-336
	@Test
	public void shouldCreateTenantWithUniqueAbbr() {

		responseTenant = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, Tenant.class);
		String tenantId = responseTenant.getTenantId();

        //Preparation for Cleanup
        idsForAllCreatedElements.add(tenantId);

		Tenant committedTenant = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, tenantId, apiRequestHelper, tenantAPI, Tenant.class);
		CustomAssertions.assertRequestAndResponseObj(responseTenant, committedTenant);
		
		//Assert that a group was created in IDM db for the tenant; the group name should be set to tenantId
		String groupId = tenantUtil.getIDMGroupForTenant(tenantId);
		
		//TODO:
		//Assert the groupId is not empty

	}

	// RREHM-982
	@Test
	public void shouldCreateTenantWithUniqueAbbrThatContainsSpecialChars() {
		String defaultAbbr = requestTenant.getAbbreviation();
		requestTenant.setAbbreviation(APITestUtil.SPECIAL_CHARS+defaultAbbr);
		
		responseTenant = MAbstractAPIHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, Tenant.class);
		String tenantId = responseTenant.getTenantId();

		//Preparation for Cleanup
		idsForAllCreatedElements.add(tenantId);

		Tenant committedTenant = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, tenantId, apiRequestHelper, tenantAPI, Tenant.class);
		CustomAssertions.assertRequestAndResponseObj(responseTenant, committedTenant);
	}

	// MOVE this to DeleteAssetTest file
	// RREHM-354
	// MOVE these two to AuthenticationAssetTest file
	// RREHM-343
	// RREHM-342

	/*
	 * POSITIVE TESTS END
	 */
}
