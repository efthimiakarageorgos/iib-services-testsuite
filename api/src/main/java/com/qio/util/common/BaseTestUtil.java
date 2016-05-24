package com.qio.util.common;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BaseTestUtil {
	protected String userName;
	protected String password;
	protected String environment;
	protected final String OAUTH_MICROSERVICE_NAME = "idm";
	protected String envRuntime;
	protected APIRequestHelper apiRequestHelper;
	protected Config userConfig;
	protected Config envConfig;
	protected Config microserviceConfig;
	protected Config envRuntimeConfig;

	public void initSetup(String userType) {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");

		userName = userConfig.getString("user." + userType + ".username");
		password = userConfig.getString("user." + userType + ".password");
		environment = envConfig.getString("env.name");
		envRuntime = envRuntimeConfig.getString("env.runtime");
		String oauthMicroservice = microserviceConfig.getString(OAUTH_MICROSERVICE_NAME + "." + envRuntime);
		apiRequestHelper = new APIRequestHelper(userName, password, oauthMicroservice);
	}

}
