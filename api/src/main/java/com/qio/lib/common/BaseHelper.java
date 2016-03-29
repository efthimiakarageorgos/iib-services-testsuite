package com.qio.lib.common;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.connection.ConnectionResponse;
import com.qio.lib.exception.ServerResponse;

public class BaseHelper {
	private ObjectMapper mapper;
		
	public String toJSONString(Object classObject) throws JsonGenerationException, JsonMappingException, IOException{
		configureMapperObject();
		return mapper.writeValueAsString(classObject);
	}
	
	public <T> T toClassObject(String jsonString, Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException{
		configureMapperObject();
		return (T) mapper.readValue(jsonString, classType);
	}
	
	private void configureMapperObject() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}
		
	public <T> T getServerResponseForInputRequest(Object requestObject, String microservice, String environment,
			APIHeaders apiRequestHeaders, MAssetTypeAPIHelper assetAPIHelper, Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException{
		String payload = toJSONString(requestObject);
		ConnectionResponse conRespPost = assetAPIHelper.create(microservice, environment, payload, apiRequestHeaders);
		return (T) toClassObject(conRespPost.getRespBody(), classType);
	}

}
