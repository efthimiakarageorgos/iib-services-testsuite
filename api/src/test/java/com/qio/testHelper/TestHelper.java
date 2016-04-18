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
	public static String TWOFIFTYSIX_CHARS = "256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong";
	public static String FIFTYONE_CHARS= "51charlong51charlong51charlong51charlong51charSlong";
	public static int responseCodeForInputRequest;

	public static <T> T getResponseObjForCreate(BaseHelper baseHelper, Object requestObject, String microservice,
			String environment, APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType)
					throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
					IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method createMethod = apiHelperObj.getClass().getMethod("create", methodArgs);
		
		String payload = baseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPost = (ConnectionResponse) createMethod.invoke(apiHelperObj, microservice,
				environment, payload, apiRequestHeaders);
		responseCodeForInputRequest = conRespPost.getRespCode();
		return (T) baseHelper.toClassObject(conRespPost.getRespBody(), classType);
	}

	/**
	 * elementId - refers the unique GUID identifier for AssetType,
	 * AssetTypeAttribute etc.
	 */
	public static <T> T getResponseObjForRetrieve(BaseHelper baseHelper, String microservice, String environment,
			String elementId, APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType)
					throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
					IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method retrieveMethod = apiHelperObj.getClass().getMethod("retrieve", methodArgs);

		ConnectionResponse conRespGet = (ConnectionResponse) retrieveMethod.invoke(apiHelperObj, microservice,
				environment, elementId, apiRequestHeaders);
		responseCodeForInputRequest = conRespGet.getRespCode();
		return (T) baseHelper.toClassObject(conRespGet.getRespBody(), classType);
	}
	
	public static <T> void deleteRequestObj(BaseHelper baseHelper, String microservice, String environment,
			String elementId, APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType)
					throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
					IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method deleteMethod = apiHelperObj.getClass().getMethod("delete", methodArgs);

		ConnectionResponse conRespDelete = (ConnectionResponse) deleteMethod.invoke(apiHelperObj, microservice,
				environment, elementId, apiRequestHeaders);
		// responseCodeForInputRequest = conRespDelete.getRespCode();
	}

	/**
	 * This method parses out the id from the input href link, which as observed
	 * is the last part in the links.
	 * 
	 * @param hrefLink
	 * @return
	 */
	public static String getElementId(String hrefLink) {
		String[] assetTypeHrefLinkSplitArray = hrefLink.split("/");
		return assetTypeHrefLinkSplitArray[assetTypeHrefLinkSplitArray.length - 1];
	}

}
