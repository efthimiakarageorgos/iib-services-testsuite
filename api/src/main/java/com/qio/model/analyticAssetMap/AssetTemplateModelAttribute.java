package com.qio.model.analyticAssetMap;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AssetTemplateModelAttribute implements Comparable {
	private String id;
	private String analyticAttribute;
	private String assetTypeAttribute;
	private String value;

	@JsonProperty("_links")
	private Links _links;

	public AssetTemplateModelAttribute() {
		this.analyticAttribute = "";
		this.assetTypeAttribute = "";
		this.value = "";
	}

	public AssetTemplateModelAttribute(String analyticAttribute, String assetTypeAttribute, String value) {
		this.analyticAttribute = analyticAttribute;
		this.assetTypeAttribute = assetTypeAttribute;
		this.value = value;
	}

	public AssetTemplateModelAttribute(AssetTemplateModelAttribute assetTemplateModelAttribute) {
		this(assetTemplateModelAttribute.getAnalyticAttribute(), assetTemplateModelAttribute.getAssetTypeAttribute(), assetTemplateModelAttribute.getValue());
	}

	public String getAnalyticAttribute() {
		return analyticAttribute;
	}

	public void setAnalyticAttribute(String analyticAttribute) {
		this.analyticAttribute = analyticAttribute;
	}

	public String getAssetTypeAttribute() {
		return assetTypeAttribute;
	}

	public void setAssetTypeAttribute(String assetTypeAttribute) {
		this.assetTypeAttribute = assetTypeAttribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

			if (!(responseObj instanceof AssetTemplateModelAttribute) || responseObj == null)
				return false;

			Field[] fields = AssetTemplateModelAttribute.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
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
	//
	// JEET THIS needs more work
	//
	public int compareTo(Object o) {
		if (!(o instanceof AssetTemplateModelAttribute))
			throw new ClassCastException();

		AssetTemplateModelAttribute assetTemplateModelAttribute = (AssetTemplateModelAttribute) o;

		return this.analyticAttribute.compareTo(assetTemplateModelAttribute.analyticAttribute);
	}
}
