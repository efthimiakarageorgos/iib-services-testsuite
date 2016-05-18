package com.qio.model.insight.insightType;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class InsightTypeAttribute implements Comparable {
	private String id;
	private String abbreviation;
	private String name;
	private String description;
	private String unit;
	private String datatype;

	@JsonProperty("_links")
	private Links _links;

	public InsightTypeAttribute() {
		this.abbreviation = "ABBRString";
		this.name = "ABBRString Name";
		this.description = "ABBRString Desc";
		this.datatype = "String";
		this.unit = "Unit";
	}

	public InsightTypeAttribute(String abbreviation, String name, String description, String unit, String datatype) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
		this.unit = unit;
		this.datatype = datatype;
	}

	public InsightTypeAttribute(InsightTypeAttribute insightTypeAttribute) {
		this(insightTypeAttribute.getAbbreviation(), insightTypeAttribute.getName(), insightTypeAttribute.getDescription(), insightTypeAttribute.getUnit(),
				insightTypeAttribute.getDatatype());
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
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

			if (!(responseObj instanceof InsightTypeAttribute) || responseObj == null)
				return false;

			Field[] fields = InsightTypeAttribute.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal != null && !field.getName().equals("id"))
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

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof InsightTypeAttribute))
			throw new ClassCastException();

		InsightTypeAttribute insightTypeAttribute = (InsightTypeAttribute) o;

		return this.abbreviation.compareTo(insightTypeAttribute.abbreviation);
	}
}
