package com.qio.testHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.model.assetType.AssetType;


import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.log4j.Logger;


public class AssetTypeTestHelper {
	AssetType assetType;
	
	private BaseHelper baseHelper = new BaseHelper();
	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	final static Logger logger = Logger.getRootLogger();
	
	
	// This method will create an AssetType that can be then used to create an asset
	public AssetType createAssetType(String assetTypeFlavor) throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		apiRequestHeaders = new APIHeaders(userName, password);
		microservice = Microservice.ASSET.toString();
		
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		
		if (assetTypeFlavor== "WithNoAttributesAndParameters") {
			requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		} 
		if (assetTypeFlavor== "WithOneAttribute") {
			//requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(????);
		}
		if (assetTypeFlavor== "WithOneParameter") {
			//requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(????);
		}
		if (assetTypeFlavor== "WithAllAttributes") {
			requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		}
		if (assetTypeFlavor== "WithAllParameters") {
			requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		}
		if (assetTypeFlavor== "WithAllAttributesAndParameters") {
			requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributesAndParameters();
		}
		
		responseAssetType = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
		
		return responseAssetType;
	}
}


