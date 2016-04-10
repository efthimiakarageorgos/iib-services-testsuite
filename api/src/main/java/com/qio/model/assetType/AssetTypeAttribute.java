package com.qio.model.assetType;

import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AssetTypeAttribute {
	private String attributeId;
	private String abbreviation;
	private String name;
	private String description;
	private String unit;
	private String datatype;
	
	@JsonProperty("_links")
	private Links _links;
	
	public AssetTypeAttribute() {
		this.abbreviation = "ABBRString";
		this.name = "ABBRString Name";
		this.description = "ABBRString Desc";
		this.datatype = "String";
		this.unit = "";
	}
	
	public AssetTypeAttribute(String abbreviation, String name, String description, String unit,
			String datatype) {
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
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
}
