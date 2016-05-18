package com.qio.insightManagement.manageInsightActivityTypes;

import org.junit.Before;
import org.junit.BeforeClass;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;

public class GetActivityTypesTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEceryTest() {
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-XXX")) and issue in linkedIssues("RREHM-XXX")

	/*
	 * NEGATIVE TESTS START
	 */
	// RREHM-1248 ()

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1247 ()

	// RREHM-1245 ()
}
