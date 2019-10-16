/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */
package com.thecompany.qa.iib.TRYME;

import com.thecompany.qa.iib.common.BaseTestSetupAndTearDown;
import com.thecompany.qa.lib.assertions.CustomAssertions;
import com.thecompany.qa.lib.common.MAbstractAPIHelper;
import com.thecompany.qa.lib.exception.ServerResponse;
import com.thecompany.qa.lib.iib.apiHelpers.carsaccounts.MAccountExtensionAPIHelper;
//import com.thecompany.qa.lib.iib.model.accountService.AccountExtension;
//import com.thecompany.qa.lib.iib.model.accountService.AccountList;
import com.thecompany.qa.lib.iib.model.taxService.TaxServiceResponse;
import com.thecompany.qa.lib.iib.model.taxService.helper.TaxServiceHelper;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TryMe extends BaseTestSetupAndTearDown {

    private static MAccountExtensionAPIHelper accountExtensionAPIRequest;
    private TaxServiceHelper taxServiceHelper;
    private TaxServiceResponse responseTaxService;

    private ServerResponse serverResp;

    final static Logger logger = Logger.getRootLogger();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
        baseInitSetupBeforeAllTests("primeservicescars");
        accountExtensionAPIRequest = new MAccountExtensionAPIHelper();
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
        baseCleanUpAfterAllTests(accountExtensionAPIRequest);
    }

    // Matching test cases in Test Case Management (Jira/Zephyr):
    // issuetype = Test AND issue in (linkedIssues(JIRA-1193)) AND issue in (linkedIssues(JIRA-950), linkedIssues(JIRA-951))


    // JIRA-1268 ()
    @Test
    public void shouldImportLegalEntityAccount() {

        //getAccountList
        //searchBasedOnName and type and locate Id

        String accountId = "1234";
        serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, accountId, apiRequestHelper, accountExtensionAPIRequest, ServerResponse.class);
        CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
    }

    @Test
    public void TRYTHIS() {

        String accountId = "ThisAssetTypeDoesNotExist";
        String TRYME = MAbstractAPIHelper.getJSONResponseForRetrieve(microservice, environment, accountId, apiRequestHelper, accountExtensionAPIRequest);
        CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Account id in the URL", serverResp);
    }
}
