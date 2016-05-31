package com.qio.util.common;

import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;

public class TenantUtil extends BaseTestUtil {

	private MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
	private String MICROSERVICE_NAME = "tenant";
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private String userType = "admin";

	public Tenant createTenant() {
		initSetup(userType);
		String tenantMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		tenantHelper = new TenantHelper();
		requestTenant = tenantHelper.getTenant();

		return APITestUtil.getResponseObjForCreate(requestTenant, tenantMicroservice, environment, apiRequestHelper, tenantAPI, Tenant.class);
	}
}