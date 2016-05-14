package com.qio.model.userGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class UserGroup {
	private String name;
	private String scope;

	// returned in the response of a POST request
	@JsonProperty("usergroupid")
	private String userGroupId;

	@JsonProperty("_links")
	private Links _links;

	public UserGroup() {
	}

	@SuppressWarnings("serial")
	public UserGroup(String timeStamp) {
		this.name = "UG" + timeStamp;
		this.scope = "SCOPE";
	}

	public UserGroup(String name, String scope) {
		this.name = name;
		this.scope = scope;
	}
	
	
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
			if (!(responseObj instanceof UserGroup) || responseObj == null)
				return false;

			Field[] fields = UserGroup.class.getDeclaredFields();
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