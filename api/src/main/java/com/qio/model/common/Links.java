package com.qio.model.common;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

public class Links {
	@JsonProperty("self")
	private SelfLink selfLink;

	@JsonProperty("tenant")
	private TenantLink tenantLink;

	public SelfLink getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(SelfLink self) {
		this.selfLink = self;
	}

	public TenantLink getTenantLink() {
		return tenantLink;
	}

	public void setTenantLink(TenantLink tenantLink) {
		this.tenantLink = tenantLink;
	}

	@Override
	public boolean equals(Object responseObj) {
		Logger logger = Logger.getRootLogger();
		Boolean equalityCheckFlag = true;
		try {
			if (!(responseObj instanceof Links) || responseObj == null)
				return false;

			Field[] fields = Links.class.getDeclaredFields();
			for (Field field : fields) {
				Object requestVal = field.get(this);
				Object responseVal = field.get(responseObj);
				if (requestVal != null)
					if (!requestVal.equals(responseVal)) {
						equalityCheckFlag = false;
						logger.info("Class Name: " + this.getClass().getName() + " --> Match failed on property: "
								+ field.getName() + ", Request Value: " + requestVal + ", Response Value: "
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

	/**
	 * TODO: Here we just need to check that the format of url is correct, i.e.
	 * it should be like https://<>/<>/ etc
	 */

	public class HrefLinks {
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

			if (!(responseObj instanceof HrefLinks) || responseObj == null)
				return false;

			String requestHref = this.getHref();
			String responseHref = ((HrefLinks) responseObj).getHref();

			if (requestHref != null)
				if (!requestHref.equals(responseHref)) {
					equalityCheckFlag = false;
					logger.info("Class Name: " + this.getClass().getName()
							+ " --> Match failed on property: href, Request Value: " + requestHref
							+ ", Response Value: " + responseHref);
				}
			return equalityCheckFlag;
		}

		/**
		 * TODO: Here we just need to check that the format of url is correct,
		 * i.e. it should be like https://<>/<>/ etc
		 */
	}

	public class SelfLink extends HrefLinks {
	}

	public class TenantLink extends HrefLinks {
	}
}
