package com.qio.model.asset;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public abstract class Asset {
	protected String abbreviation;
	protected String name;
	protected String description;
	protected String status;
	protected String tenant;
	protected String createdDate;

	@JsonProperty("_links")
	protected Links _links;

	public Asset() {
	}

	@SuppressWarnings("serial")
	public Asset(String timeStamp, String tenant) {
		this.abbreviation = "A" + timeStamp;
		this.name = "A" + timeStamp + "Name";
		this.description = "A" + timeStamp + "Desc";
		this.status = "AssetCreated";
		this.tenant = tenant;
	}

	public Asset(String abbreviation, String name, String description, String tenant, String status) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.tenant = tenant;
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
	 * If two objects do not match, then its simply going to print out their string representations in the logger message. I need to figure out a better way for this.
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
				// Checking for the format of the Date Field.
				if (field.getName().equals("createdDate")) {
					if (!(isDateCorrectlyFormatted((String) field.get(this)) && isDateCorrectlyFormatted((String) field.get(responseObj))))
						return false;
				}

				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal != null)
					if (!requestVal.equals(responseVal)) {
						equalityCheckFlag = false;
						logger.error("Class Name: " + this.getClass().getName() + " --> Match failed on property: " + field.getName() + ", Request Value: " + requestVal + ", Response Value: "
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

	public boolean isDateCorrectlyFormatted(String inputDate) {
		Logger logger = Logger.getRootLogger();
		Boolean dateFormatCheckerFlag = true;
		String dateFormatCheckerRegex = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])T(00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])Z$";

		if (!inputDate.matches(dateFormatCheckerRegex)) {
			dateFormatCheckerFlag = false;
			logger.error("Incorrectly formatted Date: " + inputDate);
		}
		return dateFormatCheckerFlag;
	}
}