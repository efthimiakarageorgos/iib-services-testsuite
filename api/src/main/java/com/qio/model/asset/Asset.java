package com.qio.model.asset;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class Asset {
	private String abbreviation;
	private String name;
	private String description;
	private String status;
	private String assetType;
	private String tenant;

	// returned in the response of a POST request
	@JsonProperty("assetid")
	private String assetId;

	@JsonProperty("_links")
	private Links _links;

	public Asset() {
	}

	@SuppressWarnings("serial")
	public Asset(String timeStamp, String assetType, String tenant) {
		this.abbreviation = "A" + timeStamp;
		this.name = "A" + timeStamp + "Name";
		this.description = "A" + timeStamp + "Desc";
		this.status = "AssetCreated";
		this.assetType = assetType;
		this.tenant = tenant;
	}

	public Asset(String abbreviation, String name, String description, String assetType, String tenant, String status) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.assetType = assetType;
		this.tenant = tenant;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	
	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
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

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their
	 * string representations in the logger message. I need to figure out a
	 * better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;
		try {
			if (!(responseObj instanceof Asset) || responseObj == null)
				return false;

			Field[] fields = Asset.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal != null)
					if (!requestVal.equals(responseVal)) {
						equalityCheckFlag = false;
						logger.info("Class Name: " + this.getClass().getName() + " --> Match failed on property: "
								+ field.getName() + ", Request Value: " + requestVal + ", Response Value: "
								+ responseVal);
						break;
					}
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
		return equalityCheckFlag;
	}
}