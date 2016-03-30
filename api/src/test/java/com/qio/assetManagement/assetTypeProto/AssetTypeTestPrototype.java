package com.qio.assetManagement.assetTypeProto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.AssetTypeParameter;

public class AssetTypeTestPrototype {
	
	@Test
	public void createAndRetrieveAssetType() throws JsonGenerationException, JsonMappingException, IOException{
	MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	BaseHelper baseHelper = new BaseHelper();
	String userName = "technician";
	String password = "user@123";
	
	String microservice = "asset-types";
	String environment = ".qiotec.internal";

	APIHeaders apiRequestHeaders = new APIHeaders(userName, password);
	
	// Creating Asset Type Attributes
	List<AssetTypeAttribute> assetTypeAttr = new ArrayList<AssetTypeAttribute>();
	
	// Creating Asset Type Parameters
		List<AssetTypeParameter> assetTypePar = new ArrayList<AssetTypeParameter>();
	
	// Creating an Asset Type
	AssetType newAsset = new AssetType("ABBRAPIAutomation", "NAMEAPIAutomation", "DESCAPIAutomation", assetTypeAttr, assetTypePar);
	String payload = baseHelper.toJSONString(newAsset);
	ConnectionResponse conRespPost = assetTypeAPI.create(microservice, environment, payload, apiRequestHeaders);
	AssetType assetTypeCreated = baseHelper.toClassObject(conRespPost.getRespBody(), AssetType.class);
	
	// Retrieving recently created asset type.
	ConnectionResponse conRespGet = assetTypeAPI.retrieve(microservice, environment, apiRequestHeaders, assetTypeCreated.getAssetTypeId());
	AssetType assetTypeRetrieved = baseHelper.toClassObject(conRespGet.getRespBody(), AssetType.class);
	
	// Deleting recently created asset type
	assetTypeAPI.delete(microservice, environment, apiRequestHeaders, assetTypeCreated.getAssetTypeId());

	}
}
