package com.qio.model.analyticAssetMap.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

//import com.jcraft.jsch.Logger;
import org.apache.log4j.Logger;
import com.qio.model.analyticAssetMap.AnalyticAssetMap;
import com.qio.model.analyticAssetMap.AssetTemplateModelAttribute;
import com.qio.model.analyticAssetMap.AnalyticInputParameter;

public class AnalyticAssetMapHelper {
	AnalyticAssetMap analyticAssetMap = null;
	final static Logger logger = Logger.getRootLogger();

	/*
	 * This method is invoked from each of the following methods to make sure every time a new analytic asset map is created with a unique timestamp.
	 */
	private void initDefaultAnalyticAssetMap() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		analyticAssetMap = new AnalyticAssetMap(timestamp, null);
	}

	@SuppressWarnings("serial")
	//IS THIS REQUIRED? WHAT DOES IT DO?
	public AnalyticAssetMap getAnalyticAssetMapWithDefaultAssetTemplateModelAttribute(String assetId, String analyticId) {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithNoAssetTemplateModelAttribute(String assetId, String analyticId) {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithNoAnalyticInputParameters(String assetId, String analyticId) {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}

	public AnalyticAssetMap getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters(String assetId, String analyticId) {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		return analyticAssetMap;
	}
	
	public AssetTemplateModelAttribute getAssetTemplateModelAttributeWithAssetAttributeLink(String analyticAttribute, String assetTypeAttribute, String value) {
		return new AssetTemplateModelAttribute(analyticAttribute, assetTypeAttribute, value);
	}
	
	public AssetTemplateModelAttribute getAssetTemplateModelAttributeWithoutAssetAttributeLink(String analyticAttribute, String value) {
		return new AssetTemplateModelAttribute(analyticAttribute, null, value);
	}
	
	public AnalyticInputParameter getAnalyticInputParameter(String analyticInput, String assetTypeParameter) {
		return new AnalyticInputParameter(analyticInput, assetTypeParameter);
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithAnalyticInputParameters(Map<String, String> analyticInputs) {
		//ArrayList<String> assetTypeParameters
		List<AnalyticInputParameter> analyticInputParameterAll = new ArrayList<AnalyticInputParameter>();
		
		for (Iterator it = analyticInputs.entrySet().iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry) it.next();
	        Object key = entry.getKey();
	        Object value = entry.getValue();
	        //logger.info(key.toString()+"   "+value.toString());
	        analyticInputParameterAll.add(getAnalyticInputParameter(key.toString(), value.toString()));
	    }
		
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		analyticAssetMap.setAnalyticInputParameters(analyticInputParameterAll);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithAssetTemplateModelAttributes(List<String> analyticAttributesWithoutLinks, Map<String, String> analyticAttributesWithLinks) {
		List<AssetTemplateModelAttribute> assetTemplateModelAttributeAll = new ArrayList<AssetTemplateModelAttribute>();
		for (String analyticAttribute : analyticAttributesWithoutLinks) {
			assetTemplateModelAttributeAll.add(getAssetTemplateModelAttributeWithoutAssetAttributeLink(analyticAttribute, ""));
		}
	    
	    for (Iterator it = analyticAttributesWithLinks.entrySet().iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry) it.next();
	        Object key = entry.getKey();
	        Object value = entry.getValue();
	       // logger.info(key.toString()+"   "+value.toString());
	        assetTemplateModelAttributeAll.add(getAssetTemplateModelAttributeWithAssetAttributeLink(key.toString(), value.toString(), ""));
	    }
		
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttributeAll);
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}


	public AnalyticAssetMap getAssetTypeWithAllAttributesAndParameters(List<String> analyticAttributesWithoutLinks, Map<String, String> analyticAttributesWithLinks, Map<String, String>  analyticInputsMap) {
		AnalyticAssetMap analyticAssetMapAttrTemp = getAnalyticAssetMapWithAssetTemplateModelAttributes(analyticAttributesWithoutLinks, analyticAttributesWithLinks);
		AnalyticAssetMap analyticAssetMapParamTemp = getAnalyticAssetMapWithAnalyticInputParameters(analyticInputsMap);
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(analyticAssetMapAttrTemp.getAssetTemplateModelAttributes());
		analyticAssetMap.setAnalyticInputParameters(analyticAssetMapParamTemp.getAnalyticInputParameters());
		return analyticAssetMap;
	}
}