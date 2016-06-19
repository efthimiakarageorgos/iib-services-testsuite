package com.qio.model.assetType;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AssetTypeParameter implements Comparable {
	private String id; // this parameter is specifically used for the PUT requests and hence is retained.
	private String parameterId;
	private String abbreviation;
	private String description;
	private String baseuom;
	private String datatype;

	@JsonProperty("_links")
	private Links _links;

	public AssetTypeParameter() {
		this.abbreviation = "ABBRString";
		this.description = "ABBRString Desc";
		this.datatype = "String";
		this.baseuom = "Unit";
	}

	public AssetTypeParameter(String abbreviation, String description, String baseuom, String datatype) {
		this.abbreviation = abbreviation;
		this.description = description;
		this.baseuom = baseuom;
		this.datatype = datatype;
	}

	public AssetTypeParameter(AssetTypeParameter assetTypeParameter) {
		this(assetTypeParameter.getAbbreviation(), assetTypeParameter.getDescription(), assetTypeParameter.getBaseuom(), assetTypeParameter
				.getDatatype());
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBaseuom() {
		return baseuom;
	}

	public void setBaseuom(String baseuom) {
		this.baseuom = baseuom;
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

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
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

			if (!(responseObj instanceof AssetTypeParameter) || responseObj == null)
				return false;

			Field[] fields = AssetTypeParameter.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				// Following if condition skips the match on 'id' property.
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
		if (!(o instanceof AssetTypeParameter))
			throw new ClassCastException();

		AssetTypeParameter assetTypeParameter = (AssetTypeParameter) o;

		return this.abbreviation.compareTo(assetTypeParameter.abbreviation);
	}
}
