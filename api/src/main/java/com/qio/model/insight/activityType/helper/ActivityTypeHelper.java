package com.qio.model.insight.activityType.helper;

import com.qio.model.insight.activityType.ActivityType;

public class ActivityTypeHelper {
	ActivityType activityType = null;

	/*
	 * This method is invoked from each of the following methods to make sure every time a new ActivityType is created with a unique timestamp.
	 */
	private void initDefaultActivityType() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		activityType = new ActivityType(timestamp);
	}

	public ActivityType getActivityType() {
		initDefaultActivityType();
		return activityType;
	}

}