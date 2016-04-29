package com.qio.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BaseTestSetupAndTearDown {

	protected static BaseHelper baseHelper;
	protected static MAssetTypeAPIHelper assetTypeAPI;
	protected static String userName;
	protected static String password;
	protected static String microservice;
	protected static String environment;
	protected static APIHeaders apiRequestHeaders;

	protected static ArrayList<String> idsForAllCreatedElements;

	public static void baseInitSetupBeforeAllTests(String microservice) {
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");

		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		BaseTestSetupAndTearDown.microservice = microservice;
		apiRequestHeaders = new APIHeaders(userName, password);

		baseHelper = new BaseHelper();
		idsForAllCreatedElements = new ArrayList<String>();
	}

	public static void baseCleanUpAfterAllTests(Object apiHelperObj) throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException,
			SecurityException, IOException {
		for (String assetTypeId : idsForAllCreatedElements) {
			TestHelper.deleteRequestObj(microservice, environment, assetTypeId, apiRequestHeaders, apiHelperObj);
		}

	}
}
