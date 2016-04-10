package com.qio.model.tenant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class Tenant {
	private String abbreviation;
	private String name;
	private String description;
	
	// returned in the response of a POST request
	@JsonProperty("tenantid")
	private String tenantId;
	
	
	@JsonProperty("_links")
	private Links _links;
	
	public Tenant() {}

	@SuppressWarnings("serial")
	public Tenant(String timeStamp) {
		this.abbreviation = "AT" + timeStamp;
		this.name = "AT" + timeStamp + "Name";
		this.description = "AT" + timeStamp + "Desc";
	}
	
	public Tenant(String abbreviation, String name, String description) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
	}
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
}
