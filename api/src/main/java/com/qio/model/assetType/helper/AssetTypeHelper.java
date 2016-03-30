package com.qio.model.assetType.helper;

import java.util.ArrayList;
import java.util.List;

import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.AssetTypeParameter;

public class AssetTypeHelper {
	AssetType assetType;
	
	// creates an assetType with default values for assetType, its attributes and its parameters.
	public AssetTypeHelper(){
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		assetType = new AssetType(timestamp);
	}

	public AssetType getAssetTypeWithNoAttributes(){
		assetType.setAttributes(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithNoAttributesAndParameters(){
		assetType.setAttributes(null);
		assetType.setParameters(null);
		return assetType;
	}

	public AssetType getAssetTypeWithOneAttribute(AttributeDataType attributeDataType){
		String attributeDataTypePrefix = "ABBR" + attributeDataType.toString();
		List<AssetTypeAttribute> assetTypeAttributeSingle = new ArrayList<AssetTypeAttribute>();
		
		assetTypeAttributeSingle.add(new AssetTypeAttribute(attributeDataTypePrefix,
				attributeDataTypePrefix + " Name",
				attributeDataTypePrefix + " Desc",
				"",
				attributeDataType.toString()));
		assetType.setAttributes(assetTypeAttributeSingle);
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithAllAttributes(){
		List<AssetTypeAttribute> assetTypeAttributeAll = new ArrayList<AssetTypeAttribute>();
		for(AttributeDataType dataType: AttributeDataType.values()){
			assetTypeAttributeAll.addAll((getAssetTypeWithOneAttribute(dataType)).getAttributes());
		}
		assetType.setAttributes(assetTypeAttributeAll);
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithDefaultAttribute(){
		assetType.setParameters(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithOneParameter(ParameterDataType parameterDataType){
		String parameterDataTypePrefix = "ABBR" + parameterDataType.toString();
		List<AssetTypeParameter> assetTypeParameterSingle = new ArrayList<AssetTypeParameter>();
		
		assetTypeParameterSingle.add(new AssetTypeParameter(parameterDataTypePrefix,
				parameterDataTypePrefix + " Desc",
				"Unit",
				parameterDataType.toString()));
		assetType.setParameters(assetTypeParameterSingle);
		assetType.setAttributes(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithAllParameters(){
		List<AssetTypeParameter> assetTypeParameterAll = new ArrayList<AssetTypeParameter>();
		for(ParameterDataType dataType: ParameterDataType.values()){
			assetTypeParameterAll.addAll((getAssetTypeWithOneParameter(dataType)).getParameters());
		}
		assetType.setParameters(assetTypeParameterAll);
		assetType.setAttributes(null);
		return assetType;
	}
	
	public AssetType getAssetTypeWithDefaultParameter(){
		assetType.setAttributes(null);
		return assetType;
	}
}
