package com.qio.tenantManagement.manageTenants;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.qio.tenantManagement.helper.TenantTestHelper;
import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class ManageTenants {

	private BaseHelper baseHelper = new BaseHelper();
	private  MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	private Tenant requestTenant2;
	private ServerResponse serverResp;

	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		microservice = Microservice.TENANT.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
	}
	
	@Before
	public void initSetupBeforeEceryTest(){
		// Initializing a new set of objects before each test case.
		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		responseTenant = new Tenant();
		serverResp = new ServerResponse();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-338 (Tenant Name is null - missing)
	@Ignore
	public void shouldNotCreateTenantWhenAbbreviationIsNotUnique() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant2 = tenantHelper.getTenant();
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant2, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		requestTenant = tenantHelper.getTenant();
		
		String tenantAbbr2 = requestTenant2.getAbbreviation();
									
		// Setting Tenant abbreviation to be the same as the name of tenant2
		requestTenant.setAbbreviation(tenantAbbr2);
									
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(409,
				null,
				"Creating tenant failed, as another tenant has same abbreviation.",
				serverResp);
		//GetTenant should not return two tenants with the same abbreviation
		//No OpenAM group should be created for the tenant that was added second
	}
		
	// RREHM-352 (Tenant abbreviation contains spaces)
	@Test
	public void shouldNotCreateTenantWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
			
		// Setting Tenant abbreviation to contain spaces
		String defaultAbbr=requestTenant.getAbbreviation();
		requestTenant.setAbbreviation("Abrr has a space"+defaultAbbr);
			
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation should not have Space or Tab.",
				serverResp);
		//GetTenant should not return a tenant with Abbreviation as specified above
		//No OpenAM group should be created
	}
	
		
	// RREHM-334 (Tenant abbreviation is null - missing)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
		String defaultAbbr=requestTenant.getAbbreviation();
		
		// Setting Tenant abbreviation to null
		requestTenant.setAbbreviation(null);
		requestTenant.setName("ThisTenantShouldNotGetCreated"+defaultAbbr);
								
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation is required, should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name as specified above
		//No OpenAM group should be created
	}
	
	// RREHM-334 (Tenant abbreviation is empty)
	@Test
	public void shouldNotCreateTenantWhenAbbreviationIsEmpty() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
		String defaultAbbr=requestTenant.getAbbreviation();
		
		// Setting Tenant abbreviation to empty
		requestTenant.setAbbreviation("");
		requestTenant.setName("ThisTenantShouldNotGetCreated"+defaultAbbr);
									
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation is required, should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name as specified above.
		//No OpenAM group should be created
	}
	
	// RREHM-984 (Tenant abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateTenantWhenAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
				
		// Setting tenant abbr to be longer than 50 chars
		requestTenant.setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
				
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Abbreviation should be less than 50 characters.",
				serverResp);
		//GetTenant should not return a tenant with abbr as specified above.
		//No OpenAM group should be created
	}
	
	
	// RREHM-833 (Tenant name is blank)
	@Test
	public void shouldNotCreateTenantWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
								
		// Setting tenant name to empty
		requestTenant.setName("");
								
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Tenant name is required, should be less than 255 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name equal to ""
	}
		
	// RREHM-833 (Tenant Name is null - missing)
	@Test
	public void shouldNotCreateTenantWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
								
		// Setting Tenant name to null
		requestTenant.setName("");
								
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(400,
				null,
				"Tenant name is required, should be less than 255 characters.",
				serverResp);
		//GetTenant should not return a tenant with Name equal to null
		
	}
	
	// RREHM-985 (Tenant name is longer than 255 chars)
	@Test
	public void shouldNotCreateTenantWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestTenant = tenantHelper.getTenant();
					
		// Setting tenant name to be longer than 255 chars
		requestTenant.setName("256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");
					
		serverResp = TenantTestHelper.getTenantCreateResponseObj(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, ServerResponse.class);
		
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
	
}
