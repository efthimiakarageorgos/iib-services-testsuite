package com.qio.model.insight;

public class InsightRequest extends Insight {
	private String insightType;

	public InsightRequest() {
	}

	@SuppressWarnings("serial")
	public InsightRequest(String timeStamp, String insightType, String tenant) {
		super(timeStamp, tenant, insightType);
		this.insightType = insightType;
	}

	public InsightRequest(String title, String description, String insightType, String tenant, String status) {
		super(title, description, tenant, status);
		this.insightType = insightType;
	}

	public InsightRequest(InsightRequest insightRequest) {
		this(insightRequest.getTitle(), insightRequest.getDescription(), insightRequest.getInsightType(), insightRequest.getTenant(), insightRequest.getStatus());
	}

	public String getInsightType() {
		return insightType;
	}

	public void setInsightType(String insightType) {
		this.insightType = insightType;
	}
}