package com.qio.assetManagement.manageAssetTypeParameters;

import org.junit.Before;
import org.junit.BeforeClass;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.common.Microservice;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;


public class GetAssetTypeParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;
	
	@BeforeClass
	public static void initSetupBeforeAllTests(){
		baseInitSetupBeforeAllTests(Microservice.ASSET.toString());
		assetTypeAPI = new MAssetTypeAPIHelper();
	}
	
	@Before
	public void initSetupBeforeEveryTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
		serverResp = new ServerResponse();
	}
	
	// The following test cases go here:
	// issuetype = Test AND issue in (linkedIssues(RREHM-1192)) AND issue in (linkedIssues(RREHM-952), linkedIssues(RREHM-953))
	
	/*
	 * NEGATIVE TESTS START
	 */
	
	// RREHM-1259 ()
	// RREHM-1255 ()
	// RREHM-1254 ()
	
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-1258 ()
	
	// RREHM-1257 ()
	
	/*
	 * POSITIVE TESTS END
	 */

}
