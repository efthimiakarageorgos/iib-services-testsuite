/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */
package com.thecompany.qa.iib.TRYME;

import com.thecompany.qa.iib.common.BaseTestSetupAndTearDown;
import com.thecompany.qa.lib.assertions.CustomAssertions;
import com.thecompany.qa.lib.common.MAbstractAPIHelper;
import com.thecompany.qa.lib.exception.ServerResponse;
import com.thecompany.qa.lib.iib.apiHelpers.taxService.MTaxServiceAPIHelper;
import com.thecompany.qa.lib.iib.model.taxService.TaxServiceLineItem;
import com.thecompany.qa.lib.iib.model.taxService.TaxServiceRequest;
import com.thecompany.qa.lib.iib.model.taxService.TaxServiceResponse;
import com.thecompany.qa.lib.iib.model.taxService.helper.TaxServiceHelper;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TryMe extends BaseTestSetupAndTearDown {

    private static MTaxServiceAPIHelper taxServiceAPI;
    private TaxServiceHelper taxServiceHelper;
    private TaxServiceRequest requestTaxService;
    private TaxServiceLineItem taxServiceLineItem;
    private ArrayList<TaxServiceLineItem> line_item;
    private TaxServiceResponse responseTaxService;

    private ServerResponse serverResp;

    final static Logger logger = Logger.getRootLogger();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
        baseInitSetupBeforeAllTests("amicroservice");
        taxServiceAPI = new MTaxServiceAPIHelper();
    }

    @Before
    public void initSetupBeforeEveryTest() {
        // Initializing a new set of objects before each test case.
        taxServiceHelper = new TaxServiceHelper();
        logger.info("ZZZZZZZZZZZZZZZZZ");
        responseTaxService = new TaxServiceResponse();
        serverResp = new ServerResponse();
    }

    @AfterClass
    public static void cleanUpAfterAllTests() {
        baseCleanUpAfterAllTests(taxServiceAPI);
    }

    // Matching test cases in Test Case Management (Jira/Zephyr):
    // issuetype = Test AND issue in (linkedIssues(JIRA-1193)) AND issue in (linkedIssues(JIRA-950), linkedIssues(JIRA-951))


    // JIRA-1268 ()
    @Test
    public void shouldGetAnErrorMsgWhenTryingToGetAttributesForANonExistingAssetType() {

        String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
        serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, taxServiceAPI, ServerResponse.class);
        CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
    }

    @Test
    public void TRYTHIS() {

        String invalidAssetTypeId = "ThisAssetTypeDoesNotExist";
        String TRYME = MAbstractAPIHelper.getJSONResponseForRetrieve(microservice, environment, invalidAssetTypeId, apiRequestHelper, taxServiceAPI);
        CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
    }
/*
	@Test
	public void shouldGetAnErrorMsgWhenTryingToGetAttributesForAnExistingAssetTypeThatHasNoAttributesConfigured() {

		//TODO
		//If the message does not match it fails instead of giving the a nicer message (same as when the code does not match - not a bid deal though)
		CustomAssertions.assertRequestAndResponseObj(200, MAbstractAPIHelper.responseCodeForInputRequest, requestTaxService.getRequest(), responseTaxService.getResponse());
		//CustomAssertions.assertRequestAndResponseObj(responseTaxService, requestTaxService);
		//CustomAssertions.assertServerError(400, "com.thecompany.application.exceptions.InvalidInputException", "No Attributes are Associated with a given Asset Type", serverResp);
	}
	*/
}
