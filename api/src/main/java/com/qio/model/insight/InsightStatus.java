package com.qio.model.insight;

public enum InsightStatus {
	OPEN("Open"),
	CLOSED("Closed");
	
	private String realValue;

	private InsightStatus(String realValue) {
		this.realValue = realValue;
	}

	@Override
	public String toString() {
		return realValue;
	}
}
