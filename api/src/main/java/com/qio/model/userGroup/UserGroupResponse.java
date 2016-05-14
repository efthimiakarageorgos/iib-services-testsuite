package com.qio.model.userGroup;

import com.qio.model.assetType.AssetType;

public class UserGroupResponse extends UserGroup {
	private UserGroup userGroup;

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their
	 * string representations in the logger message. I need to figure out a
	 * better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Boolean equalityCheckFlag = false;
		UserGroup userGroupFromResponseObj = ((UserGroupResponse) responseObj).getUserGroup();

		if (super.equals(responseObj) && this.userGroup.equals(userGroupFromResponseObj))
			equalityCheckFlag = true;

		return equalityCheckFlag;
	}
}