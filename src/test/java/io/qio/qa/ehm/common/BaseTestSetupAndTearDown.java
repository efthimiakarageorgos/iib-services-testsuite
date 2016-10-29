/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.qio.qa.lib.apiHelpers.APIRequestHelper;
//import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.common.MAbstractAPIHelper;
import org.apache.log4j.Logger;

import java.util.ArrayList;

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
	protected static ArrayList<String> secondaryIdListForDeletion;
	final static Logger logger = Logger.getRootLogger();

	public static void baseInitSetupBeforeAllTests(String microserviceName) {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");

		username = userConfig.getString("user.superuser.username");
		password = userConfig.getString("user.superuser.password");
		
		environment = envConfig.getString("env.name");
		envRuntime = envRuntimeConfig.getString("env.runtime");
		
		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);
		oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntime);
		
		apiRequestHelper = new APIRequestHelper(username, password, oauthMicroservice);

		idsForAllCreatedElements = new ArrayList<String>();
		secondaryIdListForDeletion = new ArrayList<String>();
	}

	public static void baseCleanUpAfterAllTests(Object apiHelperObj) {
		for (String elementId : idsForAllCreatedElements) {
			MAbstractAPIHelper.deleteRequestObj(microservice, environment, elementId, apiRequestHelper, apiHelperObj);
		}
	}

	public static void baseCleanUpAfterAllTests(ArrayList<String> idListForDeletion, Object apiHelperObj) {
		for (String elementId : idListForDeletion) {
			MAbstractAPIHelper.deleteRequestObj(microservice, environment, elementId, apiRequestHelper, apiHelperObj);
		}
	}
	
	public static void baseCleanUpAfterAllTests(ArrayList<String> idListForDeletion, Object apiHelperObj, String microserviceName) {
		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);
		for (String elementId : idListForDeletion) {
			MAbstractAPIHelper.deleteRequestObj(microservice, environment, elementId, apiRequestHelper, apiHelperObj);
		}
	}
}
