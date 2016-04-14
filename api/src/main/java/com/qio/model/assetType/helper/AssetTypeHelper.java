package com.qio.model.assetType.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.BeforeClass;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.AssetTypeParameter;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.log4j.Logger;


public class AssetTypeHelper {
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
	
	// creates an assetType with default values for assetType, its attributes and its parameters.
	public AssetTypeHelper(){
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		assetType = new AssetType(timestamp);
	}
	
	/*
	 * AssetType objects with Attributes only 
	 */

	public AssetType getAssetTypeWithNoAttributes(){
		assetType.setAttributes(null);
		return assetType;
	}

	public AssetType getAssetTypeWithOneAttribute(AttributeDataType attributeDataType){
		String attributeDataTypePrefix = "ABBR" + attributeDataType.toString();
		List<AssetTypeAttribute> assetTypeAttributeSingle = new ArrayList<AssetTypeAttribute>();
		
		assetTypeAttributeSingle.add(new AssetTypeAttribute(attributeDataTypePrefix,
				attributeDataTypePrefix + " Name",
				attributeDataTypePrefix + " Desc",
				"",
				attributeDataType.toString()));
		assetType.setAttributes(assetTypeAttributeSingle);
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithAllAttributes(){
		List<AssetTypeAttribute> assetTypeAttributeAll = new ArrayList<AssetTypeAttribute>();
		for(AttributeDataType dataType: AttributeDataType.values()){
			assetTypeAttributeAll.addAll((getAssetTypeWithOneAttribute(dataType)).getAttributes());
		}
		assetType.setAttributes(assetTypeAttributeAll);
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithDefaultAttribute(){
		assetType.setParameters(null);
		return assetType;
	}
	
	/*
	 * AssetType objects with Parameters only 
	 */
	
	public AssetType getAssetTypeWithNoParameters(){
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithOneParameter(ParameterDataType parameterDataType){
		String parameterDataTypePrefix = "ABBR" + parameterDataType.toString();
		List<AssetTypeParameter> assetTypeParameterSingle = new ArrayList<AssetTypeParameter>();
		
		assetTypeParameterSingle.add(new AssetTypeParameter(parameterDataTypePrefix,
				parameterDataTypePrefix + " Desc",
				"Unit",
				parameterDataType.toString()));
		assetType.setParameters(assetTypeParameterSingle);
		assetType.setAttributes(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithAllParameters(){
		List<AssetTypeParameter> assetTypeParameterAll = new ArrayList<AssetTypeParameter>();
		for(ParameterDataType dataType: ParameterDataType.values()){
			assetTypeParameterAll.addAll((getAssetTypeWithOneParameter(dataType)).getParameters());
		}
		assetType.setParameters(assetTypeParameterAll);
		assetType.setAttributes(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithDefaultParameter(){
		assetType.setAttributes(null);
		return assetType;
	}
	
	/*
	 * AssetType objects with Attributes and Parameters 
	 */
	
	public AssetType getAssetTypeWithNoAttributesAndParameters(){
		assetType.setAttributes(null);
		assetType.setParameters(null);
		return assetType;
	}

	public AssetType getAssetTypeWithAllAttributesAndParameters(){
		AssetType assetTypeAttrTemp = getAssetTypeWithAllAttributes();
		AssetType assetTypeParamTemp = getAssetTypeWithAllParameters();
		assetType.setAttributes(assetTypeAttrTemp.getAttributes());
		assetType.setParameters(assetTypeParamTemp.getParameters());
		return assetType;
	}
	
	
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
