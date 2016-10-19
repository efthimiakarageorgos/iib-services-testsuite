package com.qio.model.insight;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public abstract class Insight {
	protected String title;
	protected String description;
	protected String status;
	protected String tenant;
	protected String insightType;
	protected String createdDate;
	protected String lastModifiedDate;
	protected String referenceId;

	@JsonProperty("_links")
	protected Links _links;

	public Insight() {
	}

	@SuppressWarnings("serial")
	public Insight(String timeStamp, String tenant, String insightType) {
		this.title = "I" + timeStamp + "Title";
		this.description = "I" + timeStamp + "Desc";
		this.tenant = tenant;
		this.insightType = insightType;
	}

	public Insight(String title, String description, String tenant, String insightType) {
		//this.abbreviation = abbreviation;
		this.title = title;
		this.description = description;
		this.tenant = tenant;
		this.insightType = insightType;
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

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}
	
	public String getReferenceId() {
		return referenceId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;
		try {
			if (!(responseObj instanceof Insight) || responseObj == null)
				return false;

			Field[] fields = Insight.class.getDeclaredFields();
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
		String dateFormatCheckerRegex = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])T(00|0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])Z$";

		if (!inputDate.matches(dateFormatCheckerRegex)) {
			dateFormatCheckerFlag = false;
			logger.error("Incorrectly formatted Date: " + inputDate);
		}
		return dateFormatCheckerFlag;
	}
}