package com.qio.model.insight.activityType;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class ActivityType {
	private String abbreviation;
	private String activityName;
	private String activityDescription;
	private String status;
	final static Logger logger = Logger.getRootLogger();

	// returned in the response of a POST request
	@JsonProperty("activityTypeId")
	private String activityTypeId;

	@JsonProperty("_links")
	private Links _links;

	public ActivityType() {
	}

	@SuppressWarnings("serial")
	public ActivityType(String timeStamp) {
		this.abbreviation = "IAT" + timeStamp;
		this.activityName = "IAT" + timeStamp + "Name";
		this.activityDescription = "IAT" + timeStamp + "Desc";
	}

	public ActivityType(String abbreviation, String activityName, String activityDescription) {
		this.abbreviation = abbreviation;
		this.activityName = activityName;
		this.activityDescription = activityDescription;
	}

	public ActivityType(ActivityType activityType) {
		this(activityType.getAbbreviation(), activityType.getActivityName(), activityType.getActivityDescription());
	}

	public String getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(String activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public Links get_links() {
		return _links;
	}

	public void set_links(Links _links) {
		this._links = _links;
	}

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their string representations in the logger message. I need to figure out a
	 * better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;
		try {
			if (!(responseObj instanceof ActivityType) || responseObj == null)
				return false;

			Field[] fields = ActivityType.class.getDeclaredFields();
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
