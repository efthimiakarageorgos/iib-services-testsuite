/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */
package com.thecompany.qa.iib.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.thecompany.qa.lib.apiHelpers.APIRequestHelper;
import com.thecompany.qa.lib.common.BaseHelper;
import com.thecompany.qa.lib.idm.apiHelpers.MUserGroupAPIHelper;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

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
	protected static MUserGroupAPIHelper groupAPI;
	protected static Config userConfig;
	protected static Config envConfig;
	protected static Config envRuntimeConfig;
	protected static Config microserviceConfig;

	protected static String mongoDbServer;
	protected static String mongoDbServerPort;
	protected static String mongoDb;
	protected static String mongoUsername;
	protected static String mongoPassword;

	protected static Config mongoDbConfig;

	protected static String cassandraDbServerAddress;
	protected static String cassandraDbServerPort;
	protected static String cassandraDbKeySpace;
	protected static String cassandraUsername;
	protected static String cassandraPassword;

	protected static Config cassandraDbConfig;

	protected static String sftphostname;
	protected static String sftpusername;
	protected static String sftppassword;
	protected static int sftpport;

	protected static Config sftpConfig;

	protected static String s3secretKey;
	protected static String s3accessKey;

	protected static Config s3Config;

	protected static ArrayList<String> idsForAllCreatedElements;
	final static Logger logger = Logger.getRootLogger();

	private Description description=Description.createSuiteDescription(this.getClass());

	@Rule
	public TestWatcher watchman= new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			logger.info("Failure: " + description.getMethodName());
		}

		@Override
		protected void succeeded(Description description) {
			logger.info("Success: " + description.getMethodName());
		}
	};

	// REMOVE THIS ONE AFTER YOU REPLACE IT with the next two in the dependent files
	public static void baseInitSetupBeforeAllTests(String microserviceName) {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");

		username = userConfig.getString("user."+microserviceName+".gen.username");
		password = userConfig.getString("user."+microserviceName+".gen.password");
		password=password.replace('%', '#');

		logger.info("    AFTER "+password);

		///environment = envConfig.getString("env.name");
		envRuntime = envRuntimeConfig.getString("env.runtime");
		environment = envConfig.getString("env.name" + "." + envRuntime);
		
		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);
		//oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntime);
		
		apiRequestHelper = new APIRequestHelper(username, password, oauthMicroservice);
		//groupAPI = new MUserGroupAPIHelper();

		idsForAllCreatedElements = new ArrayList<String>();
	}

	public static void baseInitLoadConfigurationFiles() {
		userConfig = ConfigFactory.load("user_creds.conf");
		envConfig = ConfigFactory.load("environments.conf");
		envRuntimeConfig = ConfigFactory.load("environment_runtime.conf");
		microserviceConfig = ConfigFactory.load("microservices.conf");
		mongoDbConfig = ConfigFactory.load("mongodbs.conf");
		cassandraDbConfig = ConfigFactory.load("cassandradbs.conf");
		sftpConfig = ConfigFactory.load("sftp.conf");
		s3Config = ConfigFactory.load("s3.conf");

		envRuntime = envRuntimeConfig.getString("env.runtime");
	}

	public static void baseInitAPISetupBeforeAllTests(String microserviceName) {
		environment = envConfig.getString("env.name" + "." + envRuntime);

		username = userConfig.getString("user.admin.username");
		password = userConfig.getString("user.admin.password");
		oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntime);
		apiRequestHelper = new APIRequestHelper(username, password, oauthMicroservice);

		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);

		idsForAllCreatedElements = new ArrayList<>();
	}

	public static void baseInitMongoSetupBeforeAllTests(String mongoName) {
		mongoUsername = mongoDbConfig.getString(mongoName+".db.user."+envRuntime);
		mongoPassword = mongoDbConfig.getString(mongoName+".db.password."+envRuntime);
		mongoDbServer = mongoDbConfig.getString(mongoName+".db.server."+envRuntime);
		mongoDbServerPort = mongoDbConfig.getString(mongoName+".db.serverPort."+envRuntime);
		mongoDb = mongoDbConfig.getString(mongoName+".db."+envRuntime);
	}

	public static void baseInitCassandraSetupBeforeAllTests(String cassandraName) {
		cassandraUsername = cassandraDbConfig.getString(cassandraName+".db.user."+envRuntime);
		cassandraPassword = cassandraDbConfig.getString(cassandraName+".db.password."+envRuntime);
		cassandraDbServerAddress = cassandraDbConfig.getString(cassandraName+".db.serverAddress."+envRuntime);
		cassandraDbServerPort = cassandraDbConfig.getString(cassandraName+".db.serverPort."+envRuntime);
		cassandraDbKeySpace = cassandraDbConfig.getString(cassandraName+".db.keySpace."+envRuntime);
	}

	public static void baseInitSftpSetupBeforeAllTests(String sftpName) {
		sftphostname = sftpConfig.getString(sftpName+".db.hostname."+envRuntime);
		sftpusername = sftpConfig.getString(sftpName+".db.username."+envRuntime);
		sftppassword = sftpConfig.getString(sftpName+".db.password."+envRuntime);
		sftpport = sftpConfig.getInt(sftpName+".db.port."+envRuntime);
	}

	public static void baseInitS3SetupBeforeAllTests(String s3Name) {
		s3secretKey = s3Config.getString(s3Name+".s3.secretKey."+envRuntime);
		s3accessKey = s3Config.getString(s3Name+".s3.accessKey."+envRuntime);

	}

	public static void baseCleanUpAfterAllTests(Object apiHelperObj) {
		logger.info("1 AAAAAAAA argument");
		BaseHelper.deleteListOfCollectionItems(microservice, environment, apiRequestHelper, apiHelperObj, idsForAllCreatedElements);
	}

	public static void baseCleanUpAfterAllTests(ArrayList<String> idListForDeletion, Object apiHelperObj) {
		logger.info("2 arguments");
		BaseHelper.deleteListOfCollectionItems(microservice, environment, apiRequestHelper, apiHelperObj, idListForDeletion);
	}

	public static void baseCleanUpAfterAllTests(ArrayList<String> idListForDeletion, Object apiHelperObj, String microserviceName) {
		logger.info("3 arguments");
		microservice = microserviceConfig.getString(microserviceName + "." + envRuntime);
		BaseHelper.deleteListOfCollectionItems(microservice, environment, apiRequestHelper, apiHelperObj, idListForDeletion);
	}
}
