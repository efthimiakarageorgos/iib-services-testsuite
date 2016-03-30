package com.qio.model.assetType;

import org.codehaus.jackson.annotate.JsonProperty;

public class AssetTypeParameter {
	private String parameterId;
	private String abbreviation;
	private String description;
	private String baseuom;
	private String datatype;
	
	@JsonProperty("_links")
	private Links _links;
	
	public AssetTypeParameter() {
		this.abbreviation = "ABBRString";
		this.description = "ABBRString Desc";
		this.datatype = "String";
		this.baseuom = "Unit";
	}
	
	public AssetTypeParameter(String abbreviation, String description, String baseuom,
			String datatype) {
		this.abbreviation = abbreviation;
		this.description = description;
		this.baseuom = baseuom;
		this.datatype = datatype;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getbaseuom() {
		return baseuom;
	}
	public void setbaseuom(String baseuom) {
		this.baseuom = baseuom;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getParameterId() {
		return parameterId;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
}
