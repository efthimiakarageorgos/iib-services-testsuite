package com.qio.model.asset.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.model.asset.Asset;
import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;

import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;

import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AssetHelper {
	Asset asset;
	private BaseHelper baseHelper = new BaseHelper();
	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	
	private  MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	
	private static String userName;
	private static String password;
	private static String microserviceAT;
	private static String microserviceT;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	
	// creates an asset that links to an assetType and tenant.
	public AssetHelper(String assetTypeFlavor)throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
		
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		microserviceAT = Microservice.ASSET_TYPE.toString();
		microserviceT = Microservice.TENANT.toString();
		apiRequestHeaders = new APIHeaders(userName, password);
		
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		
		//Create Asset Type
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
		
		responseAssetType = TestHelper.getResponseObjForCreate(baseHelper, requestAssetType, microserviceAT, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
		String assetTypeHref=responseAssetType.get_links().getSelf().getHref();
		
		int i=assetTypeHref.lastIndexOf("/");
		int length=assetTypeHref.length();
		String assetTypeId=assetTypeHref.substring(i+1, length);
		
		//Create Tenant
		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		responseTenant = new Tenant();
		
		requestTenant = tenantHelper.getTenant();
		responseTenant = TestHelper.getResponseObjForCreate(baseHelper, requestTenant, microserviceT, environment, apiRequestHeaders, tenantAPI, Tenant.class);
		
		
		String tenantId=responseTenant.getTenantId();
		
		asset = new Asset(timestamp, assetTypeId, tenantId);
	}
	
	public Asset getAsset(){
		return asset;
	}
}
