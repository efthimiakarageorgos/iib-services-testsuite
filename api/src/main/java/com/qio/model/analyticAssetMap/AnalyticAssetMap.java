package com.qio.model.analyticAssetMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import com.qio.model.common.Links;

public class AnalyticAssetMap {
	private String lifetimeStart;
	private String lifetimeEnd;
	private Boolean enabled;
	private String comment;
	private String asset;
	private String analytic;

	// returned in the response of a POST request
	@JsonProperty("analyticassetmapid")
	private String analyticAssetMapId;

	@JsonProperty("assetTemplateModelAttributes")
	private List<AssetTemplateModelAttribute> assetTemplateModelAttributes;

	@JsonProperty("analyticInputParameters")
	private List<AnalyticInputParameter> analyticInputParameters;

	@JsonProperty("_links")
	private Links _links;

	public AnalyticAssetMap() {
	}

	@SuppressWarnings("serial")
	public AnalyticAssetMap(String timeStamp, Date date) {
		this.lifetimeStart = "2014-01-01T06:30:01.000Z";
		this.lifetimeEnd = null;
		this.enabled = true;
		this.asset = "";
		this.analytic = "";
		this.comment = "AAM" + timeStamp + "Desc";
		this.assetTemplateModelAttributes = new ArrayList<AssetTemplateModelAttribute>() {};
		this.analyticInputParameters = new ArrayList<AnalyticInputParameter>() {};
	}

	public AnalyticAssetMap(String asset, String analytic, Boolean enabled, String comment, String lifetimeStart, String lifetimeEnd, List<AssetTemplateModelAttribute> assetTemplateModelAttributes,
			List<AnalyticInputParameter> parameters) {
		this.asset = asset;
		this.analytic = analytic;
		this.enabled = enabled;
		this.comment = comment;
		this.lifetimeStart = lifetimeStart;
		this.assetTemplateModelAttributes = assetTemplateModelAttributes;
		this.analyticInputParameters = parameters;
	}

	public AnalyticAssetMap(AnalyticAssetMap analyticAssetMap) {
		this(analyticAssetMap.getAsset(), analyticAssetMap.getAnalytic(), analyticAssetMap.getEnabled(), analyticAssetMap.getComment(), analyticAssetMap.getLifetimeStart(), analyticAssetMap
				.getLifetimeEnd(), analyticAssetMap.getAssetTemplateModelAttributes(), analyticAssetMap.getAnalyticInputParameters());
	}

	public String getAnalyticAssetMap() {
		return analyticAssetMapId;
	}

	public void setAnalyticAssetMap(String analyticAssetMapId) {
		this.analyticAssetMapId = analyticAssetMapId;
	}

	public String getLifetimeStart() {
		return lifetimeStart;
	}

	public void setLifetimeStart(String lifetimeStart) {
		this.lifetimeStart = lifetimeStart;
	}

	public String getLifetimeEnd() {
		return lifetimeEnd;
	}

	public void setLifetimeEnd(String lifetimeEnd) {
		this.lifetimeEnd = lifetimeEnd;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getAnalytic() {
		return analytic;
	}

	public void setAnalytic(String analytic) {
		this.analytic = analytic;
	}

	public List<AssetTemplateModelAttribute> getAssetTemplateModelAttributes() {
		return assetTemplateModelAttributes;
	}

	public void setAssetTemplateModelAttributes(List<AssetTemplateModelAttribute> assetTemplateModelAttributes) {
		this.assetTemplateModelAttributes = assetTemplateModelAttributes;
	}

	public List<AnalyticInputParameter> getAnalyticInputParameters() {
		return analyticInputParameters;
	}

	public void setAnalyticInputParameters(List<AnalyticInputParameter> analyticInputParameters) {
		this.analyticInputParameters = analyticInputParameters;
	}

	public Links get_links() {
		return _links;
	}

	public void set_links(Links _links) {
		this._links = _links;
	}

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their string representations in the logger message. I need to figure out a better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;
		try {
			if (!(responseObj instanceof AnalyticAssetMap) || responseObj == null)
				return false;

			Field[] fields = AnalyticAssetMap.class.getDeclaredFields();
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
}
