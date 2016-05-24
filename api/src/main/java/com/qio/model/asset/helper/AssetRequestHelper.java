package com.qio.model.asset.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.model.asset.AssetRequest;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AttributeDataType;
import com.qio.model.assetType.helper.ParameterDataType;
import com.qio.model.tenant.Tenant;
import com.qio.util.common.APITestUtil;
import com.qio.util.common.AssetTypeUtil;
import com.qio.util.common.TenantUtil;

public class AssetRequestHelper {
	AssetRequest asset = null;
	AssetTypeUtil assetTypeUtil = null;
	TenantUtil tenantUtil = null;

	private void initDefaultAsset() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		asset = new AssetRequest(timestamp, "", "");
	}

	public AssetRequest getAssetWithPredefinedAssetTypeAndTenant(String assetTypeId, String tenantId) {
		initDefaultAsset();
		asset.setAssetType(assetTypeId);
		asset.setTenant(tenantId);
		return asset;
	}

	public AssetRequest getAssetWithCreatingAssetTypeAndTenant(String assetTypeFlavor, AttributeDataType attributeDataType, ParameterDataType parameterDataType) throws JsonGenerationException,
			JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		assetTypeUtil = new AssetTypeUtil();
		tenantUtil = new TenantUtil();

		AssetType assetType = assetTypeUtil.createAssetType(assetTypeFlavor, attributeDataType, parameterDataType);
		String assetTypeId = APITestUtil.getElementId(assetType.get_links().getSelfLink().getHref());

		Tenant tenant = tenantUtil.createTenant();
		String tenantId = tenant.getTenantId();

		return getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
	}
}