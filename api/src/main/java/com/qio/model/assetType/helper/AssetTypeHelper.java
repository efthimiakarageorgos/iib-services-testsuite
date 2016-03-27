package com.qio.model.assetType.helper;

import java.util.ArrayList;
import java.util.List;

import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;

public class AssetTypeHelper {
	AssetType assetType;
	
	// creates an assetType with default values for assetType and its attribute.
	public AssetTypeHelper(){
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		assetType = new AssetType(timestamp);
	}

	public AssetType getAssetTypeWithNoAttributes(){
		assetType.setAttributes(null);
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
		return assetType;
	}
	
	public AssetType getAssetTypeWithAllAttributes(){
		List<AssetTypeAttribute> assetTypeAttributeAll = new ArrayList<AssetTypeAttribute>();
		for(AttributeDataType dataType: AttributeDataType.values()){
			assetTypeAttributeAll.addAll((getAssetTypeWithOneAttribute(dataType)).getAttributes());
		}
		assetType.setAttributes(assetTypeAttributeAll);
		return assetType;
	}
	
	public AssetType getAssetTypeWithDefaultAttribute(){
		return assetType;
	}
}
