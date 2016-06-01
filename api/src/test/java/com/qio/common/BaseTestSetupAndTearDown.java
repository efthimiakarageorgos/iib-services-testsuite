package com.qio.common;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.util.common.APITestUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BaseTestSetupAndTearDown {

	protected static String username;
	protected static String password;
	protected static String microservice;
	protected static String oauthMicroservice;
	protected static String environment;
	protected static String envRuntime;
	protected static String oauthMicroserviceName = "idm";
	protected static APIRequestHelper apiRequestHelper;
	protected static Config userConfig;
	protected static Config envConfig;
	protected static Config envRuntimeConfig;
	protected static Config microserviceConfig;

	protected static ArrayList<String> idsForAllCreatedElements;
	final static Logger logger = Logger.getRootLogger();

	public static void baseInitSetupBeforeAllTests(String microserviceName) {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");

		username = userConfig.getString("user.test.username");
		password = userConfig.getString("user.test.password");
		environment = envConfig.getString("env.name");
		envRuntime = envRuntimeConfig.getString("env.runtime");
		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);
		oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntime);
		apiRequestHelper = new APIRequestHelper(username, password, oauthMicroservice);

		idsForAllCreatedElements = new ArrayList<String>();
	}

	public static void baseCleanUpAfterAllTests(Object apiHelperObj) {
		for (String elementId : idsForAllCreatedElements) {
			APITestUtil.deleteRequestObj(microservice, environment, elementId, apiRequestHelper, apiHelperObj);
		}
	}

	public static void baseCleanUpAfterAllTests(ArrayList<String> idsForAllCreatedElements, Object apiHelperObj) {
		for (String elementId : idsForAllCreatedElements) {
			APITestUtil.deleteRequestObj(microservice, environment, elementId, apiRequestHelper, apiHelperObj);
		}
	}
}
