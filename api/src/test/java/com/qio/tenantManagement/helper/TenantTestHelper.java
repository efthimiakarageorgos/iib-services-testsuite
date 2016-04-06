package com.qio.tenantManagement.helper;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;

public class TenantTestHelper {
	
	public static String SPECIAL_CHARS = "~^%{&@}$#*()+=!~";

	public static <T> T getTenantCreateResponseObj(BaseHelper baseHelper, Object requestObject, String microservice,
			String environment, APIHeaders apiRequestHeaders, MTenantAPIHelper tenantAPIHelper,
			Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException{
		
		String payload = baseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPost = tenantAPIHelper.create(microservice, environment, payload, apiRequestHeaders);
		return (T) baseHelper.toClassObject(conRespPost.getRespBody(), classType);
	}
}
