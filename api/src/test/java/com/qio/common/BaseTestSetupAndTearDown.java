package com.qio.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BaseTestSetupAndTearDown {

	protected static String username;
	protected static String password;
	protected static String microservice;
	protected static String environment;
	protected static String envRuntime;
	protected static APIHeaders apiRequestHeaders;
	protected static Config userConfig;
	protected static Config envConfig;
	protected static Config envRuntimeConfig;
	protected static Config microserviceConfig;

	protected static ArrayList<String> idsForAllCreatedElements;

	public static void baseInitSetupBeforeAllTests(String microservice) {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");

		username = userConfig.getString("user.test.username");
		password = userConfig.getString("user.test.password");
		environment = envConfig.getString("env.name");
		envRuntime = envRuntimeConfig.getString("env.runtime");
		BaseTestSetupAndTearDown.microservice = microserviceConfig.getString(microservice + "." + envRuntime);
		apiRequestHeaders = new APIHeaders(username, password, envRuntime);

		idsForAllCreatedElements = new ArrayList<String>();
	}

	public static void baseCleanUpAfterAllTests(Object apiHelperObj) throws JsonGenerationException, JsonMappingException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		for (String assetTypeId : idsForAllCreatedElements) {
			TestHelper.deleteRequestObj(microservice, environment, assetTypeId, apiRequestHeaders, apiHelperObj);
		}

	}
}
