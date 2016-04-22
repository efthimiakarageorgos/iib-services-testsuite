package com.qio.common;

import java.util.ArrayList;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
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

	protected static ArrayList<String> idsForAllCreatedAssetTypes;

	public static void baseInitSetupBeforeAllTests(String microservice) {
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");

		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		BaseTestSetupAndTearDown.microservice = microservice;
		apiRequestHeaders = new APIHeaders(userName, password);

		baseHelper = new BaseHelper();
	}
}
