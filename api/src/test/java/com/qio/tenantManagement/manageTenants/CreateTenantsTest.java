package com.qio.tenantManagement.manageTenants;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.apiHelpers.idm.MUserGroupAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;
import com.qio.util.common.APITestUtil;

public class CreateTenantsTest extends BaseTestSetupAndTearDown {

	private static MTenantAPIHelper tenantAPI;
	private static MUserGroupAPIHelper groupAPI;
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	private Tenant requestTenant2;
	private ServerResponse serverResp;

	// private UserGroupHelper groupHelper;
	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("tenant");
		username = userConfig.getString("user.admin.username");
		password = userConfig.getString("user.admin.password");
		apiRequestHelper.setUserName(username);
		apiRequestHelper.setPassword(password);
		tenantAPI = new MTenantAPIHelper();
		groupAPI = new MUserGroupAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		requestTenant = tenantHelper.getTenant();
		responseTenant = new Tenant();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {

		// Currently no user is allowed to delete a tenant
		baseCleanUpAfterAllTests(tenantAPI);
		// JEET : THIS DOES NOT WORK as it uses the asset microservice to make the call and not the idm microservice
		baseCleanUpAfterAllTests(idsForAllCreatedElements, groupAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1191")) and issue in linkedIssues("RREHM-37")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-338 (Tenant with no unique abbreviation)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() {
		requestTenant2 = tenantHelper.getTenant();
		serverResp = APITestUtil.getResponseObjForCreate(requestTenant2, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		String tenantAbbr2 = requestTenant2.getAbbreviation();

		// Setting Tenant abbreviation to be the same as the name of tenant2
		requestTenant.setAbbreviation(tenantAbbr2);
		logger.info(microservice);
		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(409, null, "Creating tenant failed, as another tenant has same abbreviation.", serverResp);
		// GetTenant should not return two tenants with the same abbreviation
		// No IDM group should be created for the tenant that was added second
	}

	// RREHM-352 (Tenant abbreviation contains spaces)
	@Test
	public void shouldNotCreateTenantWhenAbbrContainsSpaces() {
		String defaultAbbr = requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("Abrr has a space" + defaultAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should not have Space or Tab.", serverResp);
		// GetTenant should not return a tenant with Abbreviation as specified above
		// No IDM group should be created
	}

	// RREHM-334 (Tenant abbreviation is null - missing)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNull() {
		String defaultAbbr = requestTenant.getAbbreviation();

		// Setting Tenant abbreviation to null
		requestTenant.setAbbreviation(null);
		requestTenant.setName("ThisTenantShouldNotGetCreated" + defaultAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

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

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

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

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Abbreviation should be less than 50 characters.", serverResp);
		// GetTenant should not return a tenant with abbr as specified above.
		// No IDM group should be created
	}

	// RREHM-833 (Tenant name is blank)
	@Test
	public void shouldNotCreateTenantWhenNameIsBlank() {

		requestTenant.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name is required, should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to ""
	}

	// RREHM-833 (Tenant Name is null - missing)
	@Test
	public void shouldNotCreateTenantWhenNameIsNull() {

		requestTenant.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name is required, should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to null

	}

	// RREHM-985 (Tenant name is longer than 255 chars)
	@Test
	public void shouldNotCreateTenantWhenNameIsLongerThan255Chars() {

		requestTenant.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, ServerResponse.class);

		CustomAssertions.assertServerError(400, null, "Tenant name should be less than 255 characters.", serverResp);
		// GetTenant should not return a tenant with Name equal to name specified above
	}
	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */

	//
	// MORE ASSERTIONS NEEDED
	// system generated elements: There should be a ReferenceId element with unique value that is an increment of the previously created tenant
	// Need to asset that a group got created - idm service - see attempt below
	//

	// RREHM-336
	@Test
	public void shouldCreateTenantWithUniqueAbbr() {

		responseTenant = APITestUtil.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHelper, tenantAPI, Tenant.class);
		String tenantId = responseTenant.getTenantId();
		idsForAllCreatedElements.add(tenantId);
		Tenant committedTenant = APITestUtil.getResponseObjForRetrieve(microservice, environment, tenantId, apiRequestHelper, tenantAPI, Tenant.class);
		CustomAssertions.assertRequestAndResponseObj(responseTenant, committedTenant);

		// JEET THIS FAILS because the groups are corrupted on DEV... RREHM-2198
		// UserGroup committedGroup = APITestUtil.getResponseObjForRetrieve(oauthMicroservice, environment, tenantId, apiRequestHelper, groupAPI, UserGroup.class);
	}

	// RREHM-982

	// MOVE this to DeleteAssetTest file
	// RREHM-354
	// MOVE these two to AuthenticationAssetTest file
	// RREHM-343
	// RREHM-342

	/*
	 * POSITIVE TESTS END
	 */
}
