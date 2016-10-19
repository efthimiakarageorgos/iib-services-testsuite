package com.qio.util.common;

import com.qio.lib.apiHelpers.insights.MInsightTypeAPIHelper;
import com.qio.model.insight.insightType.InsightType;
import com.qio.model.insight.insightType.helper.InsightTypeHelper;
import com.qio.model.insight.insightType.helper.AttributeDataType;

public class InsightTypeUtil extends BaseTestUtil {

	private MInsightTypeAPIHelper insightTypeAPI = new MInsightTypeAPIHelper();
	private InsightTypeHelper insightTypeHelper;
	private InsightType requestInsightType;
	private final String MICROSERVICE_NAME = "insights";
	private String userType = "test";

	/**
	 * 
	 * @param insightTypeFlavor
	 * @param attributeDataType
	 *            -- will require a valid AttributeDataType for the option WithOneAttribute, else pass in null
	 */
	public InsightType createInsightType(String insightTypeFlavor, AttributeDataType attributeDataType) {
		initSetup(userType);
		String insightTypeMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		insightTypeHelper = new InsightTypeHelper();

		switch (insightTypeFlavor) {
		case "WithNoAttributes":
			requestInsightType = insightTypeHelper.getInsightTypeWithNoAttributes();
			break;
		case "WithOneAttribute":
			requestInsightType = insightTypeHelper.getInsightTypeWithOneAttribute(attributeDataType);
			break;
		case "WithAllAttributes":
			requestInsightType = insightTypeHelper.getInsightTypeWithAllAttributes();
			break;
		default:
			requestInsightType = insightTypeHelper.getInsightTypeWithNoAttributes();
		}
		return APITestUtil.getResponseObjForCreate(requestInsightType, insightTypeMicroservice, environment, apiRequestHelper, insightTypeAPI, InsightType.class);
	}
}