package com.qio.model.assetType;

public class AssetTypeAttributeDatatype {
	private String type;
	
	public AssetTypeAttributeDatatype() {
		this.type = "String";
	}
	
	public AssetTypeAttributeDatatype(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
