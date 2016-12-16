/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.dictionaryManagement.manageDictionaries;

import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.cassandraHelpers.MAbstractCassandraHelper;
import io.qio.qa.lib.common.MAbstractAPIHelper;
import io.qio.qa.lib.exception.ServerResponse;
import io.qio.qa.lib.ehm.apiHelpers.dictionary.MDictionaryAPIHelper;
import io.qio.qa.lib.ehm.common.AssetTypeUtil;
import io.qio.qa.lib.ehm.common.AssetUtil;
import io.qio.qa.lib.ehm.model.asset.AssetResponse;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.dictionary.Dictionary;
import io.qio.qa.lib.ehm.model.dictionary.helper.DictionaryHelper;
import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;


public class CreateDictionariesTest extends BaseTestSetupAndTearDown {
	private static ArrayList<String> idsForAllCreatedAssets;
	private static ArrayList<String> idsForAllCassandraInserts;

    private static MDictionaryAPIHelper dictionaryAPI;
    private static DictionaryHelper dictionaryHelper;
    private static Dictionary dictionaryRequest;
    private static Dictionary dictionaryResponse;
	private static AssetUtil assetUtil;
	private static AssetTypeUtil assetTypeUtil;
    private static ServerResponse serverResp;
	AssetResponse asset;

	private static String assetId;
	private static String tenantId;
	private static String parameterId;
	private static AssetType newAssetType;

    final static Logger logger = Logger.getRootLogger();
	private static MAbstractCassandraHelper cassandraObject = new MAbstractCassandraHelper();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
		baseInitLoadConfigurationFiles();
		baseInitAPISetupBeforeAllTests("dictionary");
		////////baseInitCassandraSetupBeforeAllTests("ingestion");

		username = userConfig.getString("user.admin.username");
        password = userConfig.getString("user.admin.password");
        apiRequestHelper.setUserName(username);
        apiRequestHelper.setPassword(password);

        dictionaryAPI = new MDictionaryAPIHelper();
		dictionaryHelper = new DictionaryHelper();

		idsForAllCreatedAssets = new ArrayList<>();
		idsForAllCassandraInserts = new ArrayList<>();

		assetTypeUtil = new AssetTypeUtil();
		assetUtil = new AssetUtil();
		newAssetType = assetTypeUtil.createAssetTypeBasedOnExistingAssetType(assetTypeUtil.getAssetTypeIdBasedOnAbbreviationSearch("DHP"), "Automation", "Automation Desc");
		logger.info("xxxxxx");
	}

    @Before
    public void initSetupBeforeEveryTest() {
        dictionaryResponse = new Dictionary();
        serverResp = new ServerResponse();
		asset = assetUtil.createAssetWithPredefinedAssetTypeAndWithCreatingTenant(newAssetType.getAssetTypeId());
		assetId = asset.getAssetId();
		tenantId = asset.getTenant();

		idsForAllCreatedAssets.add(assetId);
    }

    @AfterClass
    public static void cleanUpAfterAllTests() {
		//////cassandraObject.executeCQLInCassandra(idsForAllCassandraInserts, cassandraDbServerAddress, cassandraDbKeySpace, cassandraUsername, cassandraPassword);
		//assetUtil.deleteAssetCollectionItemsAndDependencies(idsForAllCreatedAssets);
    }

	// Matching test cases in Test Case Management (Jira/Zephyr):
	// issuetype=Test and issue in (linkedIssues("LRRF-XXXXX"))

	/*
	 * NEGATIVE TESTS START
	 */

	// LRRF-XXX
	@Test
	public void XXXX() {
		dictionaryRequest = dictionaryHelper.getDictionaryWithPredefinedAssetAndTenantForParameter(assetId, tenantId, parameterId, "XXX");
//
//		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestTenant2, microservice, environment, apiRequestHelper, dictionaryAPI, ServerResponse.class);
//
//		// Setting Tenant abbreviation to be the same as the name of tenant2
//		dictionaryRequest.setAbbreviation(tenantAbbr2);
//		serverResp = MAbstractAPIHelper.getResponseObjForCreate(dictionaryRequest, microservice, environment, apiRequestHelper, dictionaryAPI, ServerResponse.class);
//
//		CustomAssertions.assertServerError(409, null, "Creating tenant failed, as another tenant has same abbreviation.", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */
	

	/*
	 * POSITIVE TESTS START
	 */

	// LRRF-216
	@Test
	public void shouldNotCreateDictionaryEntryWithATagThatIsUniqueAcrossAllTenants() {
		String parameterId = asset.getAssetType().getParameters().get(0).getId();
		String parameterType = asset.getAssetType().getParameters().get(0).getDatatype();

		dictionaryRequest = dictionaryHelper.getDictionaryWithPredefinedAssetAndTenantForParameter(assetId, tenantId, parameterId, parameterType);


		dictionaryResponse = MAbstractAPIHelper.getResponseObjForCreate(dictionaryRequest, microservice, environment, tenantId, apiRequestHelper, dictionaryAPI, Dictionary.class);
		logger.info(dictionaryResponse.getTenant());

		logger.info(dictionaryResponse.toCassandraInsert());

//		//Preparation for Cleanup
//		idsForAllCreatedElements.add(organizationId+":"+tenantId);
//
		Dictionary committedDictionaryEntry = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, tenantId, apiRequestHelper, dictionaryAPI, Dictionary.class);
		CustomAssertions.assertRequestAndResponseObj(dictionaryResponse, committedDictionaryEntry);
	}
	/*
	 * POSITIVE TESTS END
	 */
}
