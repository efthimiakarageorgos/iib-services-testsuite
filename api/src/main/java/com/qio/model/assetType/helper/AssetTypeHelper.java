package com.qio.model.assetType.helper;

import java.util.ArrayList;
import java.util.List;

import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.AssetTypeAttribute;
import com.qio.model.assetType.AssetTypeParameter;

public class AssetTypeHelper {
	AssetType assetType = null;

	/*
	 * This method is invoked from each of the following methods to make sure every time a new assettype is created with a unique timestamp.
	 */
	private void initDefaultAssetType() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		assetType = new AssetType(timestamp);
	}

	/*
	 * AssetType objects with Attributes only
	 */

	public AssetType getAssetTypeWithNoAttributes() {
		initDefaultAssetType();
		assetType.setAttributes(null);
		return assetType;
	}

	@SuppressWarnings("serial")
	public AssetType getAssetTypeWithOneAttribute(AttributeDataType attributeDataType) {
		initDefaultAssetType();
		assetType.setAttributes(new ArrayList<AssetTypeAttribute>() {
			{
				add(getAssetTypeAttributeWithInputDataType(attributeDataType));
			}
		});
		assetType.setParameters(null);
		return assetType;
	}

	public AssetType getAssetTypeWithAllAttributes() {
		List<AssetTypeAttribute> assetTypeAttributeAll = new ArrayList<AssetTypeAttribute>();
		for (AttributeDataType dataType : AttributeDataType.values()) {
			assetTypeAttributeAll.add(getAssetTypeAttributeWithInputDataType(dataType));
		}
		initDefaultAssetType();
		assetType.setAttributes(assetTypeAttributeAll);
		assetType.setParameters(null);
		return assetType;
	}

	public AssetTypeAttribute getAssetTypeAttributeWithInputDataType(AttributeDataType attributeDataType) {
		String attributeDataTypePrefix = "ABBR" + attributeDataType.toString();
		return new AssetTypeAttribute(attributeDataTypePrefix, attributeDataTypePrefix + " Name", attributeDataTypePrefix + " Desc", "",
				attributeDataType.toString());
	}

	public AssetType getAssetTypeWithDefaultAttribute() {
		initDefaultAssetType();
		assetType.setParameters(null);
		return assetType;
	}

	/*
	 * AssetType objects with Parameters only
	 */

	public AssetType getAssetTypeWithNoParameters() {
		initDefaultAssetType();
		assetType.setParameters(null);
		return assetType;
	}

	@SuppressWarnings("serial")
	public AssetType getAssetTypeWithOneParameter(ParameterDataType parameterDataType) {
		initDefaultAssetType();
		assetType.setParameters(new ArrayList<AssetTypeParameter>() {
			{
				add(getAssetTypeParameterWithInputDataType(parameterDataType));
			}
		});
		assetType.setAttributes(null);
		return assetType;
	}

	public AssetType getAssetTypeWithAllParameters() {
		List<AssetTypeParameter> assetTypeParameterAll = new ArrayList<AssetTypeParameter>();
		for (ParameterDataType dataType : ParameterDataType.values()) {
			assetTypeParameterAll.add(getAssetTypeParameterWithInputDataType(dataType));
		}
		initDefaultAssetType();
		assetType.setParameters(assetTypeParameterAll);
		assetType.setAttributes(null);
		return assetType;
	}

	public AssetTypeParameter getAssetTypeParameterWithInputDataType(ParameterDataType parameterDataType) {
		String parameterDataTypePrefix = "ABBR" + parameterDataType.toString();
		return new AssetTypeParameter(parameterDataTypePrefix, parameterDataTypePrefix + " Desc", "Unit", parameterDataType.toString());
	}

	public AssetType getAssetTypeWithDefaultParameter() {
		initDefaultAssetType();
		assetType.setAttributes(null);
		return assetType;
	}

	/*
	 * AssetType objects with Attributes and Parameters
	 */

	public AssetType getAssetTypeWithNoAttributesAndParameters() {
		initDefaultAssetType();
		assetType.setAttributes(null);
		assetType.setParameters(null);
		return assetType;
	}

	public AssetType getAssetTypeWithAllAttributesAndParameters() {
		AssetType assetTypeAttrTemp = getAssetTypeWithAllAttributes();
		AssetType assetTypeParamTemp = getAssetTypeWithAllParameters();
		initDefaultAssetType();
		assetType.setAttributes(assetTypeAttrTemp.getAttributes());
		assetType.setParameters(assetTypeParamTemp.getParameters());
		return assetType;
	}
}