package com.qio.testHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

// TODO: This class needs to be cleaned up, as it contains code duplication.
public class TenantTestHelper {
	Tenant tenant;

	private BaseHelper baseHelper = new BaseHelper();
	private MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	protected static String oauthMicroserviceName = "idm";
	private static APIRequestHelper apiRequestHeaders;
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	final static Logger logger = Logger.getRootLogger();

	// This method will create an AssetType that can be then used to create an asset
	public Tenant createTenant() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		Config microserviceConfig = ConfigFactory.load("microservices.conf");
		Config envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");

		userName = userConfig.getString("user.test.username");
		password = userConfig.getString("user.test.password");
		environment = envConfig.getString("env.name");
		String oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntimeConfig.getString("env.runtime"));
		apiRequestHeaders = new APIRequestHelper(userName, password, oauthMicroservice);
		microservice = Microservice.TENANT.toString();

		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		responseTenant = new Tenant();
		requestTenant = tenantHelper.getTenant();

		responseTenant = TestHelper.getResponseObjForCreate(requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, Tenant.class);
		return responseTenant;
	}
}