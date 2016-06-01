package com.qio.util.common;

import com.qio.lib.apiHelpers.MAssetAPIHelper;
import com.qio.model.asset.AssetRequest;
import com.qio.model.asset.AssetResponse;
import com.qio.model.asset.helper.AssetRequestHelper;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.model.assetType.helper.ParameterDataType;

public class AssetUtil extends BaseTestUtil {

	private MAssetAPIHelper assetAPI = new MAssetAPIHelper();
	private AssetRequestHelper assetRequestHelper;
	private static AssetRequest requestAsset;
	private final String MICROSERVICE_NAME = "asset";
	private String userType = "test";

	public AssetResponse createAssetWithCreatingAssetTypeAndTenant(String assetTypeFlavor, AttributeDataType attributeDataType, ParameterDataType parameterDataType) {
		initSetup(userType);
		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant(assetTypeFlavor, attributeDataType, parameterDataType);

		return APITestUtil.getResponseObjForCreate(requestAsset, assetMicroservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
	}
}