package com.qio.model.userGroup.helper;

import java.util.ArrayList;
import java.util.List;

import com.qio.model.userGroup.UserGroup;


public class UserGroupHelper {
	UserGroup userGroup = null;

	/*
	 * This method is invoked from each of the following methods to make sure every time a new user group is created with a unique timestamp.
	 */
	private void initDefaultUserGroup() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		userGroup = new UserGroup();
	}

	public UserGroup getUserGroup() {
		initDefaultUserGroup();
		return userGroup;
	}
}