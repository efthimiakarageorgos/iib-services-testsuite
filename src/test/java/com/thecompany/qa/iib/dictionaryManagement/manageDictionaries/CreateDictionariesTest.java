/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */

/*
package com.thecompany.qa.iib.dictionaryManagement.manageDictionaries;

import com.thecompany.qa.lib.assertions.CustomAssertions;
import com.thecompany.qa.lib.cassandraHelpers.MAbstractCassandraHelper;
import com.thecompany.qa.lib.common.MAbstractAPIHelper;
import com.thecompany.qa.lib.exception.ServerResponse;
import com.thecompany.qa.lib.iib.apiHelpers.dictionary.MDictionaryAPIHelper;
import com.thecompany.qa.lib.iib.common.AssetTypeUtil;
import com.thecompany.qa.lib.iib.common.AssetUtil;
import com.thecompany.qa.lib.iib.model.asset.AssetResponse;
import com.thecompany.qa.lib.iib.model.assetType.AssetType;
import com.thecompany.qa.lib.iib.model.dictionary.Dictionary;
import com.thecompany.qa.lib.iib.model.dictionary.helper.DictionaryHelper;
import com.thecompany.qa.iib.common.BaseTestSetupAndTearDown;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.thecompany.qa.lib.common.BaseHelper.getCurrentTimeStamp;


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
        apiHeaderRequestHelper.setUserName(username);
        apiHeaderRequestHelper.setPassword(password);

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
	// issuetype=Test and issue in (linkedIssues("LRRF-XXXXX")

	/*
    // I commented this out as I had deleted some of the files i was pointing out


	/*
	 * NEGATIVE TESTS START
	 */


	/*
	 * NEGATIVE TESTS END
	 */
	

	/*
	 * POSITIVE TESTS START
	 */

	/*
	// RREHM-2295
	@Test
	public void shouldBeAbleToCreateDictionaryEntryWithATagThatIsUniqueAcrossAllTenants() {
		ArrayList<String> insertList = new ArrayList<>();

		parameterId = parameters.get(0).getParameterId();
        dictionaryRequest = dictionaryHelper.getDictionaryForPredefinedAssetAndTenantForAParameter("tag"+getCurrentTimeStamp(), assetId, tenantId, parameterId, "sourceUnit", "val");

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
//		dictionaryResponse = MAbstractAPIHelper.getResponseObjForCreate(dictionaryRequest, microservice, environment, tenantId, apiHeaderRequestHelper, dictionaryAPI, Dictionary.class);
//		CustomAssertions.assertRequestAndResponseObj(201, MAbstractAPIHelper.responseCodeForInputRequest, dictionaryRequest, dictionaryResponse);

		String tag = dictionaryRequest.getTag();
		//Preparation for Cleanup
		idsForAllCreatedElements.add(tenantId+":"+tag);
		// -------- END

		Dictionary committedDictionaryEntry = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, tenantId, tag, apiHeaderRequestHelper, dictionaryAPI, Dictionary.class);
		CustomAssertions.assertRequestAndResponseObj(dictionaryResponse, committedDictionaryEntry);
	}
} */
// I commented this out as I had deleted some of the files i was pointing out