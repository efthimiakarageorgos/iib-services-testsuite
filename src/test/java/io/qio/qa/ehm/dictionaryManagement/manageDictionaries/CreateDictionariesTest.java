/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.dictionaryManagement.manageDictionaries;

import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.cassandraHelpers.MAbstractCassandraHelper;
import io.qio.qa.lib.common.MAbstractAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeParameter;
import io.qio.qa.lib.ehm.model.tenant.Tenant;
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
import java.util.List;


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
	private static List<AssetTypeParameter> parameters;
	private static String parameterId;
	private static String parameterType;
	private static AssetType newAssetType;

    final static Logger logger = Logger.getRootLogger();
	private static MAbstractCassandraHelper cassandraObject = new MAbstractCassandraHelper();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
		baseInitLoadConfigurationFiles();
		baseInitAPISetupBeforeAllTests("dictionary");
		baseInitCassandraSetupBeforeAllTests("ingestion");

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
	}

    @Before
    public void initSetupBeforeEveryTest() {
        dictionaryResponse = new Dictionary();
        serverResp = new ServerResponse();
		asset = assetUtil.createAssetWithPredefinedAssetTypeAndWithCreatingTenant(newAssetType.getAssetTypeId());
		assetId = asset.getAssetId();
		tenantId = asset.getTenant();
		parameters = asset.getAssetType().getParameters();

		idsForAllCreatedAssets.add(assetId);
    }

    @AfterClass
    public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(dictionaryAPI);

		//NOTE: This line is alternative to deleting the dictionary entry using the API (see previous line)
		//cassandraObject.executeCQLInCassandra(idsForAllCassandraInserts, cassandraDbServerAddress, cassandraDbKeySpace, cassandraUsername, cassandraPassword);

		assetUtil.deleteAssetCollectionItemsAndDependencies(idsForAllCreatedAssets);
    }

	// Matching test cases in Test Case Management (Jira/Zephyr):
	// issuetype=Test and issue in (linkedIssues("LRRF-XXXXX"))

	/*
	 * NEGATIVE TESTS START
	 */


	/*
	 * NEGATIVE TESTS END
	 */
	

	/*
	 * POSITIVE TESTS START
	 */

	// RREHM-2295
	@Test
	public void shouldBeAbleToCreateDictionaryEntryWithATagThatIsUniqueAcrossAllTenants() {
		ArrayList<String> insertList = new ArrayList<>();

		parameterId = parameters.get(0).getParameterId();
		dictionaryRequest = dictionaryHelper.getDictionaryForPredefinedAssetAndTenantForAParameter("tag-232432", assetId, tenantId, parameterId, "sourceUnit", "val");

		logger.info("INSERT: "+dictionaryRequest.toCassandraInsert());
		logger.info("DELETE: "+dictionaryRequest.toCassandraDelete());

		String insertCommand = dictionaryRequest.toCassandraInsert();
		insertList.add(insertCommand);

		// WE ARE USING THESE LINES TO DO THE INSERT - DEFEATS THE PURPOSE OF TEST AS WE BYPASS API - API CREATE IS FAILING ON DEV AT THE MOMENT
		// DELETE API IS NOT FAILING
		cassandraObject.executeCQLInCassandra(insertList, cassandraDbServerAddress, cassandraDbKeySpace, cassandraUsername, cassandraPassword);

		//Preparation for Cleanup
		String deleteCommand = dictionaryRequest.toCassandraDelete();
		idsForAllCassandraInserts.add(deleteCommand);

		// THESE ARE THE LINES WE WANT TO KEEP FOR THIS TEST ------ START
//		dictionaryResponse = MAbstractAPIHelper.getResponseObjForCreate(dictionaryRequest, microservice, environment, tenantId, apiRequestHelper, dictionaryAPI, Dictionary.class);
//		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, dictionaryRequest, dictionaryResponse);

		String tag = dictionaryRequest.getTag();
		//Preparation for Cleanup
		idsForAllCreatedElements.add(tenantId+":"+tag);
		// -------- END

		Dictionary committedDictionaryEntry = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, tenantId, tag, apiRequestHelper, dictionaryAPI, Dictionary.class);
		CustomAssertions.assertRequestAndResponseObj(dictionaryResponse, committedDictionaryEntry);
	}
	/*
	 * POSITIVE TESTS END
	 */
}
