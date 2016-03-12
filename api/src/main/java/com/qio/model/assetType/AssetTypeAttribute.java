package com.qio.model.assetType;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AssetTypeAttribute {
	private String attributeId;
	private String abbreviation;
	private String name;
	private String description;
	private String unit;
	@JsonProperty("datatype")
	private List<AssetTypeAttributeDatatype> datatype;
	
	public AssetTypeAttribute() {
		this.abbreviation = "ABBRString";
		this.name = "ABBRString Name";
		this.description = "ABBRString Desc";
		this.datatype = new ArrayList<AssetTypeAttributeDatatype>(){{
			add(new AssetTypeAttributeDatatype());
		}};
		this.unit = "";
	}
	
	public AssetTypeAttribute(String abbreviation, String name, String description, String unit,
			List<AssetTypeAttributeDatatype> datatype) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.unit = unit;
		this.datatype = datatype;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<AssetTypeAttributeDatatype> getDatatype() {
		return datatype;
	}
	public void setDatatype(List<AssetTypeAttributeDatatype> datatype) {
		this.datatype = datatype;
	}

	public String getAttributeId() {
		return attributeId;
	}
	
}
