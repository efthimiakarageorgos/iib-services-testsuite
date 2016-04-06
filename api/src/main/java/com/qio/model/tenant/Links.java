package com.qio.model.tenant;

import org.codehaus.jackson.annotate.JsonProperty;

public class Links {
	@JsonProperty("self")
	private Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	/**
	 * Inner class to capture the Self attribute returned as a part of the _links object.
	 */
	public class Self{
		@JsonProperty("href")
		String href;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}
	}
}
