package com.qio.lib.common;

/**
 * This enum is intended to contain the list of all microservices. This will
 * ensure that the name of microservice needs to be changed at one place only,
 * in the event its changed.
 */
public enum Microservice {
	INSIGHT("insight-management"),
	DN("diagnostic-networks"),
	ASSET("assets"),
	// ASSET("assets-new-qa"),
	TENANT("tenant-management-qa");

	// ASSET("assets"),
	// TENANT("tenant-management");

	/*
	 * The following section of code does not require any change, if more
	 * microservice enum constants are added above. Make sure that all enums are
	 * separated by a comma(,) and the last one ends with a semicolon(;) e.g.
	 * ASSET("assets"), TENANT("tenant-management");
	 */
	private String realValue;

	private Microservice(String realValue) {
		this.realValue = realValue;
	}

	@Override
	public String toString() {
		return realValue;
	}
}
