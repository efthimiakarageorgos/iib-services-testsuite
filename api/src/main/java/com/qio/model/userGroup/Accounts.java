package com.qio.model.userGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class Accounts {
	private String preferredUsername;
	private String familyName;
	private String email;
	private String givenName;
	private String group;
	private String organization;

	// returned in the response of a POST request
	@JsonProperty("accountid")
	private String accountId;

	@JsonProperty("_links")
	private Links _links;

	public Accounts() {
	}

	@SuppressWarnings("serial")
	public Accounts(String timeStamp) {
		this.preferredUsername = "ACC" + timeStamp;
	}

	public Accounts(String familyName, String address, String givenName, String email, String preferredUsername) {
		this.preferredUsername = preferredUsername;
		this.familyName = familyName;
		this.givenName = givenName;
		this.preferredUsername = preferredUsername;
		this.email = email;
	}
	
	
	public String getPreferredUsername() {
		return preferredUsername;
	}

	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
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
			if (!(responseObj instanceof Accounts) || responseObj == null)
				return false;

			Field[] fields = Accounts.class.getDeclaredFields();
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