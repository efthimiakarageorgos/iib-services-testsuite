package com.qio.model.insight.helper;

import com.qio.model.insight.InsightRequest;
import com.qio.model.insight.insightType.InsightType;
import com.qio.model.insight.insightType.helper.AttributeDataType;
import com.qio.model.tenant.Tenant;
import com.qio.util.common.APITestUtil;
import com.qio.util.common.InsightTypeUtil;
import com.qio.util.common.TenantUtil;

public class InsightRequestHelper {
	InsightRequest insight = null;
	InsightTypeUtil insightTypeUtil = null;
	TenantUtil tenantUtil = null;

	private void initDefaultInsight() {
		java.util.Date date = new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		insight = new InsightRequest(timestamp, "", "");
	}

	public InsightRequest getInsightWithPredefinedInsightTypeAndTenant(String insightTypeId, String tenantId) {
		initDefaultInsight();
		insight.setInsightType(insightTypeId);
		insight.setTenant(tenantId);
		return insight;
	}

	public InsightRequest getInsightWithCreatingInsightTypeAndTenant(String insightTypeFlavor, AttributeDataType attributeDataType) {
		insightTypeUtil = new InsightTypeUtil();
		tenantUtil = new TenantUtil();

		InsightType insightType = insightTypeUtil.createInsightType(insightTypeFlavor, attributeDataType);
		String insightTypeId = APITestUtil.getElementId(insightType.get_links().getSelfLink().getHref());

		Tenant tenant = tenantUtil.createTenant();
		String tenantId = tenant.getTenantId();

		return getInsightWithPredefinedInsightTypeAndTenant(insightTypeId, tenantId);
	}
}