package com.qio.util.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.model.assetType.helper.ParameterDataType;

public class AssetTypeUtil extends BaseTestUtil {

	private MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private final String MICROSERVICE_NAME = "asset";
	private String userType = "test";

	/**
	 * 
	 * @param assetTypeFlavor
	 * @param attributeDataType
	 *            -- will require a valid AttributeDataType for the option WithOneAttribute, else pass in null
	 * @param parameterDataType
	 *            -- will require a valid ParameterDataType for the option WithOneParameter, else pass in null
	 */
	public AssetType createAssetType(String assetTypeFlavor, AttributeDataType attributeDataType, ParameterDataType parameterDataType) throws JsonGenerationException, JsonMappingException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		initSetup(userType);
		String assetTypeMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		assetTypeHelper = new AssetTypeHelper();

		switch (assetTypeFlavor) {
		case "WithNoAttributesAndParameters":
			requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
			break;
		case "WithOneAttribute":
			requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(attributeDataType);
			break;
		case "WithOneParameter":
			requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(parameterDataType);
			break;
		case "WithAllParameters":
			requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
			break;
		case "WithAllAttributesAndParameters":
			requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributesAndParameters();
			break;
		default:
			requestAssetType = assetTypeHelper.getAssetTypeWithNoAttributesAndParameters();
		}
		return APITestUtil.getResponseObjForCreate(requestAssetType, assetTypeMicroservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
	}
}