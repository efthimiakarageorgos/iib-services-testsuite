package com.qio.tenantManagement.manageTenants;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;
import com.qio.testHelper.TestHelper;


public class CreateTenantsTest extends BaseTestSetupAndTearDown {

	private static MTenantAPIHelper tenantAPI;
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	private Tenant requestTenant2;
	private ServerResponse serverResp;
	final static Logger logger = Logger.getRootLogger();

	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests("tenant");
		tenantAPI = new MTenantAPIHelper();
	}
	
	@Before
	public void initSetupBeforeEceryTest(){
		// Initializing a new set of objects before each test case.
		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		responseTenant = new Tenant();
		serverResp = new ServerResponse();
	}
	
	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1191")) and issue in  linkedIssues("RREHM-37")
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-338 (Tenant with no unique abbreviation)
	@Ignore
	public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant2 = tenantHelper.getTenant();
		serverResp = TestHelper.getResponseObjForCreate(requestTenant2, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		requestTenant = tenantHelper.getTenant();
		
		String tenantAbbr2 = requestTenant2.getAbbreviation();
									
		// Setting Tenant abbreviation to be the same as the name of tenant2
		requestTenant.setAbbreviation(tenantAbbr2);
		logger.info(microservice);	
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(409,
				null,
				"Creating tenant failed, as another tenant has same abbreviation.",
				serverResp);
		//GetTenant should not return two tenants with the same abbreviation
		//No OpenAM group should be created for the tenant that was added second
	}
		
	// RREHM-352 (Tenant abbreviation contains spaces)
	@Test
	public void shouldNotCreateTenantWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
			
		// Setting Tenant abbreviation to contain spaces
		String defaultAbbr=requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("Abrr has a space"+defaultAbbr);
			
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation should not have Space or Tab.",
				serverResp);
		//GetTenant should not return a tenant with Abbreviation as specified above
		//No OpenAM group should be created
	}
	
		
	// RREHM-334 (Tenant abbreviation is null - missing)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
		String defaultAbbr=requestTenant.getAbbreviation();
		
		// Setting Tenant abbreviation to null
		requestTenant.setAbbreviation(null);
		requestTenant.setName("ThisTenantShouldNotGetCreated"+defaultAbbr);
								
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation is required, should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name as specified above
		//No OpenAM group should be created
	}
	
	// RREHM-334 (Tenant abbreviation is empty)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsEmpty() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
		
		String defaultAbbr=requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("");
		requestTenant.setName("ThisTenantShouldNotGetCreated"+defaultAbbr);
									
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation is required, should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name as specified above.
		//No OpenAM group should be created
	}
	
	// RREHM-984 (Tenant abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateTenantWhenAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
			
		requestTenant.setAbbreviation(TestHelper.FIFTYONE_CHARS);
				
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with abbr as specified above.
		//No OpenAM group should be created
	}
	
	
	// RREHM-833 (Tenant name is blank)
	@Test
	public void shouldNotCreateTenantWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
								
		// Setting tenant name to empty
		requestTenant.setName("");
								
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Tenant name is required, should be less than 255 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name equal to ""
	}
		
	// RREHM-833 (Tenant Name is null - missing)
	@Test
	public void shouldNotCreateTenantWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
								
		// Setting Tenant name to null
		requestTenant.setName("");
								
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Tenant name is required, should be less than 255 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name equal to null
		
	}
	
	// RREHM-985 (Tenant name is longer than 255 chars)
	@Test
	public void shouldNotCreateTenantWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		requestTenant = tenantHelper.getTenant();
					
		// Setting tenant name to be longer than 255 chars
		requestTenant.setName(TestHelper.TWOFIFTYSIX_CHARS);
					
		serverResp = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Tenant name should be less than 255 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name equal to name specified above
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-336
	// RREHM-982
	
	/*
	 * POSITIVE TESTS END
	 */
}
