package com.qio.model.assetType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AssetType {
	private String abbreviation;
	private String name;
	private String description;

	// returned in the response of a POST request
	@JsonProperty("assettypeid")
	private String assetTypeId;

	@JsonProperty("attributes")
	private List<AssetTypeAttribute> attributes;

	@JsonProperty("parameters")
	private List<AssetTypeParameter> parameters;

	@JsonProperty("_links")
	private Links _links;

	public AssetType() {
	}

	@SuppressWarnings("serial")
	public AssetType(String timeStamp) {
		this.abbreviation = "AT" + timeStamp;
		this.name = "AT" + timeStamp + "Name";
		this.description = "AT" + timeStamp + "Desc";
		this.attributes = new ArrayList<AssetTypeAttribute>() {
			{
				add(new AssetTypeAttribute());
			}
		};
		this.parameters = new ArrayList<AssetTypeParameter>() {
			{
				add(new AssetTypeParameter());
			}
		};
	}

	public AssetType(String abbreviation, String name, String description, List<AssetTypeAttribute> attributes, List<AssetTypeParameter> parameters) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.attributes = attributes;
		this.parameters = parameters;
	}
	
	public AssetType(AssetType assetType){
		this(assetType.getAbbreviation(), assetType.getName(), assetType.getDescription(), assetType.getAttributes(), assetType.getParameters());
	}

	public String getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(String assetTypeId) {
		this.assetTypeId = assetTypeId;
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

	public List<AssetTypeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AssetTypeAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<AssetTypeParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<AssetTypeParameter> parameters) {
		this.parameters = parameters;
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
			if (!(responseObj instanceof AssetType) || responseObj == null)
				return false;

			Field[] fields = AssetType.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal != null)
					if (!requestVal.equals(responseVal)) {
						equalityCheckFlag = false;
						logger.error("Class Name: " + this.getClass().getName() + " --> Match failed on property: "
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
