package com.qio.model.analyticAssetMap.helper;

import java.util.ArrayList;
import java.util.List;

import com.qio.model.analyticAssetMap.AnalyticAssetMap;
import com.qio.model.analyticAssetMap.AssetTemplateModelAttribute;
import com.qio.model.analyticAssetMap.AnalyticInputParameter;

public class AnalyticAssetMapHelper {
	AnalyticAssetMap analyticAssetMap = null;

	/*
	 * This method is invoked from each of the following methods to make sure every time a new analytic asset map is created with a unique timestamp.
	 */
	private void initDefaultAnalyticAssetMap() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		analyticAssetMap = new AnalyticAssetMap(timestamp, date, "assetId", "analyticId");
	}

	@SuppressWarnings("serial")
	//IS THIS REQUIRED? WHAT DOES IT DO?
	public AnalyticAssetMap getAnalyticAssetMapWithDefaultAssetTemplateModelAttribute() {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithNoAssetTemplateModelAttribute() {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithNoAnalyticInputParameters() {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}

	public AnalyticAssetMap getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters() {
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAnalyticInputParameters(null);
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		return analyticAssetMap;
	}
	
	public AssetTemplateModelAttribute getAssetTemplateModelAttributeWithAssetAttributeLink(String analyticAttribute, String assetTypeAttribute) {
		return new AssetTemplateModelAttribute(analyticAttribute, assetTypeAttribute, null);
	}
	
	public AssetTemplateModelAttribute getAssetTemplateModelAttributeWithoutAssetAttributeLink(String analyticAttribute) {
		return new AssetTemplateModelAttribute(analyticAttribute, null, null);
	}
	
	public AnalyticInputParameter getAnalyticInputParameter(String analyticInput, String assetTypeParameter) {
		return new AnalyticInputParameter(analyticInput, assetTypeParameter);
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithAnalyticInputParameters(List<String> analyticInputs, ArrayList<String> assetTypeParameters) {
		List<AnalyticInputParameter> analyticInputParameterAll = new ArrayList<AnalyticInputParameter>();
		
		for (String analyticInputParameter : analyticInputs) {
			analyticInputParameterAll.add(getAnalyticInputParameter(analyticInputParameter, "TheAssetTypeParameter"));
		}
		
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(null);
		analyticAssetMap.setAnalyticInputParameters(analyticInputParameterAll);
		return analyticAssetMap;
	}
	
	public AnalyticAssetMap getAnalyticAssetMapWithAssetTemplateModelAttributes(List<String> analyticAttributesWithoutLinks, List<String> analyticAttributesWithLinks) {
		List<AssetTemplateModelAttribute> assetTemplateModelAttributeAll = new ArrayList<AssetTemplateModelAttribute>();
		for (String analyticAttribute : analyticAttributesWithoutLinks) {
			assetTemplateModelAttributeAll.add(getAssetTemplateModelAttributeWithoutAssetAttributeLink(analyticAttribute));
		}
		
		for (String analyticAttribute : analyticAttributesWithLinks) {
			assetTemplateModelAttributeAll.add(getAssetTemplateModelAttributeWithAssetAttributeLink(analyticAttribute, null));
		}
		
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttributeAll);
		analyticAssetMap.setAnalyticInputParameters(null);
		return analyticAssetMap;
	}


	public AnalyticAssetMap getAssetTypeWithAllAttributesAndParameters(List<String> analyticAttributesWithoutLinks, List<String> analyticAttributesWithLinks, List<String> analyticInputs, ArrayList<String> assetTypeParameters) {
		AnalyticAssetMap analyticAssetMapAttrTemp = getAnalyticAssetMapWithAssetTemplateModelAttributes(analyticAttributesWithoutLinks, analyticAttributesWithLinks);
		AnalyticAssetMap analyticAssetMapParamTemp = getAnalyticAssetMapWithAnalyticInputParameters(analyticInputs, assetTypeParameters);
		initDefaultAnalyticAssetMap();
		analyticAssetMap.setAssetTemplateModelAttributes(analyticAssetMapAttrTemp.getAssetTemplateModelAttributes());
		analyticAssetMap.setAnalyticInputParameters(analyticAssetMapParamTemp.getAnalyticInputParameters());
		return analyticAssetMap;
	}
}