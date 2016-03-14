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
import com.qio.model.assetType.AssetTypeAttributeDatatype;

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
	
	// Creating datatypes
	List<AssetTypeAttributeDatatype> assetTypeAttrDatatype1 = new ArrayList<AssetTypeAttributeDatatype>();
	assetTypeAttrDatatype1.add(new AssetTypeAttributeDatatype("Int"));
	assetTypeAttrDatatype1.add(new AssetTypeAttributeDatatype("Float"));
	
	List<AssetTypeAttributeDatatype> assetTypeAttrDatatype2 = new ArrayList<AssetTypeAttributeDatatype>();
	assetTypeAttrDatatype2.add(new AssetTypeAttributeDatatype("IntFictitious"));
	assetTypeAttrDatatype2.add(new AssetTypeAttributeDatatype("FloatFictitious"));
	
	List<AssetTypeAttributeDatatype> assetTypeAttrDatatype3 = new ArrayList<AssetTypeAttributeDatatype>();
	assetTypeAttrDatatype3.add(new AssetTypeAttributeDatatype("TestAPI1Fictitious"));
	assetTypeAttrDatatype3.add(new AssetTypeAttributeDatatype("TestAPI2Fictitious"));

	// Creating Asset Type Attributes
	List<AssetTypeAttribute> assetTypeAttr = new ArrayList<AssetTypeAttribute>();
	assetTypeAttr.add(new AssetTypeAttribute("ABBRSubTypeAPIAuto1", "NameSubTypeAPIAuto1", "DESCSubTypeAuto1", "A", assetTypeAttrDatatype1));
	assetTypeAttr.add(new AssetTypeAttribute("ABBRSubTypeAPIAuto2", "NameSubTypeAPIAuto2", "DESCSubTypeAuto2", "B", assetTypeAttrDatatype2));
	assetTypeAttr.add(new AssetTypeAttribute("ABBRSubTypeAPIAuto3", "NameSubTypeAPIAuto3", "DESCSubTypeAuto3", "C", assetTypeAttrDatatype3));
	
	// Creating an Asset Type
	AssetType newAsset = new AssetType("ABBRAPIAutomation", "NAMEAPIAutomation", "DESCAPIAutomation", assetTypeAttr);
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
