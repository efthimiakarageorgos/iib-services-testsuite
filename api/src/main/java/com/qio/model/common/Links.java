package com.qio.model.common;

import org.apache.log4j.Logger;
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

	/*
	 * Since these classes only have one field each, therefore we have used a
	 * simpler version of the equals method here. Once fields start to grow, we
	 * can follow similar implementation for this method as we have done in
	 * other classes.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;

		if (!(responseObj instanceof Links) || responseObj == null)
			return false;

		Self requestSelf = this.getSelf();
		Self responseSelf = ((Links) responseObj).getSelf();

		if (requestSelf != null)
			if (!requestSelf.equals(responseSelf)) {
				equalityCheckFlag = false;
				logger.info("Class Name: " + this.getClass().getName()
						+ " --> Match failed on property: href, Request Value: " + requestSelf + ", Response Value: "
						+ responseSelf);
			}
		return equalityCheckFlag;
	}

	/**
	 * Inner class to capture the Self attribute returned as a part of the
	 * _links object.
	 */
	public class Self {
		@JsonProperty("href")
		String href;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		@Override
		public boolean equals(Object responseObj) {
			Logger logger = Logger.getRootLogger();
			Boolean equalityCheckFlag = true;

			if (!(responseObj instanceof Self) || responseObj == null)
				return false;

			String requestHref = this.getHref();
			String responseHref = ((Self) responseObj).getHref();

			if (requestHref != null)
				if (!requestHref.equals(responseHref)) {
					equalityCheckFlag = false;
					logger.info("Class Name: " + this.getClass().getName()
							+ " --> Match failed on property: href, Request Value: " + requestHref
							+ ", Response Value: " + responseHref);
				}
			return equalityCheckFlag;
		}
	}
}
