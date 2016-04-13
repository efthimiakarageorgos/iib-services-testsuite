package com.qio.testHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;

public class TestHelper {
	
	public static String SPECIAL_CHARS = "~^%{&@}$#*()+=!~";
	public static int actualResponseCode;

	public static <T> T getResponseObjForCreate(BaseHelper baseHelper, Object requestObject, String microservice,
			String environment, APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType)
					throws JsonGenerationException, JsonMappingException, IOException,
										IllegalAccessException, IllegalArgumentException, InvocationTargetException,
										NoSuchMethodException, SecurityException{

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method createMethod = apiHelperObj.getClass().getMethod("create", methodArgs);
		
		String payload = baseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPost = (ConnectionResponse) createMethod.invoke(apiHelperObj, microservice,
				environment, payload, apiRequestHeaders);
		actualResponseCode = conRespPost.getRespCode();
		return (T) baseHelper.toClassObject(conRespPost.getRespBody(), classType);
	}
}
