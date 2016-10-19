package com.qio.model.insight;

import com.qio.model.insight.insightType.InsightType;

public class InsightResponse extends Insight {
	private InsightType insightType;

	public InsightType getInsightType() {
		return insightType;
	}

	public void setInsightType(InsightType insightType) {
		this.insightType = insightType;
	}

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their
	 * string representations in the logger message. I need to figure out a
	 * better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Boolean equalityCheckFlag = false;
		InsightType insightTypeFromResponseObj = ((InsightResponse) responseObj).getInsightType();

		if (super.equals(responseObj) && this.insightType.equals(insightTypeFromResponseObj))
			equalityCheckFlag = true;

		return equalityCheckFlag;
	}
}