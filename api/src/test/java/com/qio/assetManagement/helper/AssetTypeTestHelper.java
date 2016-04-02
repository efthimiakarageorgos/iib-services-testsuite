package com.qio.assetManagement.helper;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;

public class AssetTypeTestHelper {
	
	public static String SPECIAL_CHARS = "~^%{&@}$#*()+=!~";

	public static <T> T getAssetTypeCreateResponseObj(BaseHelper baseHelper, Object requestObject, String microservice,
			String environment, APIHeaders apiRequestHeaders, MAssetTypeAPIHelper assetTypeAPIHelper,
			Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException{
		
		String payload = baseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPost = assetTypeAPIHelper.create(microservice, environment, payload, apiRequestHeaders);
		return (T) baseHelper.toClassObject(conRespPost.getRespBody(), classType);
	}
}
