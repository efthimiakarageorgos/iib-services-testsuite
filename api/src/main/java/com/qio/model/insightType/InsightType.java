package com.qio.model.insightType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class InsightType {
	private String abbreviation;
	private String name;
	private String description;

	// returned in the response of a POST request
	@JsonProperty("insighttypeid")
	private String insightTypeId;

	@JsonProperty("attributes")
	private List<InsightTypeAttribute> attributes;

	@JsonProperty("_links")
	private Links _links;

	public InsightType() {
	}

	@SuppressWarnings("serial")
	public InsightType(String timeStamp) {
		this.abbreviation = "IT" + timeStamp;
		this.name = "IT" + timeStamp + "Name";
		this.description = "IT" + timeStamp + "Desc";
		this.attributes = new ArrayList<InsightTypeAttribute>() {
			{
				add(new InsightTypeAttribute());
			}
		};
	}

	public InsightType(String abbreviation, String name, String description, List<InsightTypeAttribute> attributes) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.attributes = attributes;
	}

	public InsightType(InsightType assetType) {
		this(assetType.getAbbreviation(), assetType.getName(), assetType.getDescription(), assetType.getAttributes());
	}

	public String getInsightTypeId() {
		return insightTypeId;
	}

	public void setInsightTypeId(String insightTypeId) {
		this.insightTypeId = insightTypeId;
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

	public List<InsightTypeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<InsightTypeAttribute> attributes) {
		this.attributes = attributes;
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
			if (!(responseObj instanceof InsightType) || responseObj == null)
				return false;

			Field[] fields = InsightType.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal instanceof List) {
					Collections.sort((List) requestVal);
					Collections.sort((List) responseVal);
				}

				if (requestVal != null)
					if (!requestVal.equals(responseVal)) {
						equalityCheckFlag = false;
						logger.error("Class Name: " + this.getClass().getName() + " --> Match failed on property: " + field.getName()
								+ ", Request Value: " + requestVal + ", Response Value: " + responseVal);
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
