package com.qio.testHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.connection.ConnectionResponse;

public class TestHelper {

	public static String SPECIAL_CHARS = "~^%{&@}$#*()+=!~";
	public static String TWOFIFTYSIX_CHARS = "256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong";
	public static String FIFTYONE_CHARS = "51charlong51charlong51charlong51charlong51charSlong";
	public static int responseCodeForInputRequest;

	final static Logger logger = Logger.getRootLogger();

	public static <T> T getResponseObjForCreate(Object requestObject, String microservice, String environment, APIHeaders apiRequestHeaders,
			Object apiHelperObj, Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException,
					IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method createMethod = apiHelperObj.getClass().getMethod("create", methodArgs);

		String payload = BaseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPost = (ConnectionResponse) createMethod.invoke(apiHelperObj, microservice, environment, payload,
				apiRequestHeaders);
		responseCodeForInputRequest = conRespPost.getRespCode();
		return (T) BaseHelper.toClassObject(conRespPost.getRespBody(), classType);
	}

	/**
	 * elementId - refers the unique GUID identifier for AssetType,
	 * AssetTypeAttribute etc.
	 */
	public static <T> T getResponseObjForRetrieve(String microservice, String environment, String elementId, APIHeaders apiRequestHeaders,
			Object apiHelperObj, Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException,
					IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method retrieveMethod = apiHelperObj.getClass().getMethod("retrieve", methodArgs);

		ConnectionResponse conRespGet = (ConnectionResponse) retrieveMethod.invoke(apiHelperObj, microservice, environment, elementId,
				apiRequestHeaders);
		responseCodeForInputRequest = conRespGet.getRespCode();
		return (T) BaseHelper.toClassObject(conRespGet.getRespBody(), classType);
	}

	public static <T> List<T> getListResponseObjForRetrieve(String microservice, String environment, String elementId, APIHeaders apiRequestHeaders,
			Object apiHelperObj, Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException,
					IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method retrieveMethod = apiHelperObj.getClass().getMethod("retrieve", methodArgs);

		ConnectionResponse conRespGet = (ConnectionResponse) retrieveMethod.invoke(apiHelperObj, microservice, environment, elementId,
				apiRequestHeaders);
		responseCodeForInputRequest = conRespGet.getRespCode();
		return (List<T>) BaseHelper.toClassObjectList(conRespGet.getRespBody(), classType);
	}

	public static <T> T getResponseObjForRetrieve(String microservice, String environment, String elementId, String subElementId,
			APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType) throws JsonGenerationException,
					JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
					NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[5];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = methodArgs[3] = String.class;
		methodArgs[4] = APIHeaders.class;
		Method retrieveMethod = apiHelperObj.getClass().getMethod("retrieve", methodArgs);

		ConnectionResponse conRespGet = (ConnectionResponse) retrieveMethod.invoke(apiHelperObj, microservice, environment, elementId, subElementId,
				apiRequestHeaders);
		responseCodeForInputRequest = conRespGet.getRespCode();
		return (T) BaseHelper.toClassObject(conRespGet.getRespBody(), classType);
	}

	public static <T> T getResponseObjForRetrieveAll(String microservice, String environment, APIHeaders apiRequestHeaders, Object apiHelperObj,
			Class<T> classType) throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
					IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[3];
		methodArgs[0] = methodArgs[1] = String.class;
		methodArgs[2] = APIHeaders.class;
		Method retrieveAllMethod = apiHelperObj.getClass().getMethod("retrieve", methodArgs);

		ConnectionResponse conRespGet = (ConnectionResponse) retrieveAllMethod.invoke(apiHelperObj, microservice, environment, apiRequestHeaders);
		responseCodeForInputRequest = conRespGet.getRespCode();
		return (T) BaseHelper.toClassObject(conRespGet.getRespBody(), classType);
	}

	public static <T> void deleteRequestObj(String microservice, String environment, String elementId, APIHeaders apiRequestHeaders,
			Object apiHelperObj) throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
					InvocationTargetException, NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[4];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = String.class;
		methodArgs[3] = APIHeaders.class;
		Method deleteMethod = apiHelperObj.getClass().getMethod("delete", methodArgs);

		ConnectionResponse conRespDelete = (ConnectionResponse) deleteMethod.invoke(apiHelperObj, microservice, environment, elementId,
				apiRequestHeaders);
		// responseCodeForInputRequest = conRespDelete.getRespCode();
	}

	public static <T> T getResponseObjForUpdate(Object requestObject, String microservice, String environment, String elementId,
			APIHeaders apiRequestHeaders, Object apiHelperObj, Class<T> classType) throws JsonGenerationException,
					JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
					NoSuchMethodException, SecurityException {

		Class[] methodArgs = new Class[5];
		methodArgs[0] = methodArgs[1] = methodArgs[2] = methodArgs[3] = String.class;
		methodArgs[4] = APIHeaders.class;
		Method updateMethod = apiHelperObj.getClass().getMethod("update", methodArgs);

		String payload = BaseHelper.toJSONString(requestObject);
		ConnectionResponse conRespPut = (ConnectionResponse) updateMethod.invoke(apiHelperObj, microservice, environment, payload, elementId,
				apiRequestHeaders);
		responseCodeForInputRequest = conRespPut.getRespCode();
		return (T) BaseHelper.toClassObject(conRespPut.getRespBody(), classType);
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

	public static String getCurrentTimeStamp() {
		java.util.Date date = new java.util.Date();
		return Long.toString(date.getTime());
	}
}
