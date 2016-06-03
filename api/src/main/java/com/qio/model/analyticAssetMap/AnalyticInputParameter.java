package com.qio.model.analyticAssetMap;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AnalyticInputParameter implements Comparable {
	private String id;
	private String assetTypeParameter;
	private String analyticInput;

	@JsonProperty("_links")
	private Links _links;

	public AnalyticInputParameter() {
		this.assetTypeParameter = "";
		this.analyticInput = "";
	}

	public AnalyticInputParameter(String analyticInput, String assetTypeParameter) {
		this.assetTypeParameter = assetTypeParameter;
		this.analyticInput = analyticInput;
	}

	public AnalyticInputParameter(AnalyticInputParameter analyticInputParameter) {
		this(analyticInputParameter.getAnalyticInput(), analyticInputParameter.getAssetTypeParameter());
	}

	public String getAssetTypeParameter() {
		return assetTypeParameter;
	}

	public void setAssetTypeParameter(String assetTypeParameter) {
		this.assetTypeParameter = assetTypeParameter;
	}

	public String getAnalyticInput() {
		return analyticInput;
	}

	public void setAnalyticInput(String analyticInput) {
		this.analyticInput = analyticInput;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

			if (!(responseObj instanceof AnalyticInputParameter) || responseObj == null)
				return false;

			Field[] fields = AnalyticInputParameter.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				// Following if condition skips the match on 'id' property.
				if (requestVal != null && !field.getName().equals("id"))
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

	@Override
	// JEET THIS NEEDS CHANGE I BELIEVE
	//
	public int compareTo(Object o) {
		if (!(o instanceof AnalyticInputParameter))
			throw new ClassCastException();

		AnalyticInputParameter analyticInputParameter = (AnalyticInputParameter) o;

		return this.analyticInput.compareTo(analyticInputParameter.analyticInput);
	}
}
