package com.qio.assetManagement.manageAssets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MAssetAPIHelper;
//AAA
import com.qio.lib.apiHelpers.idm.MUserGroupAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.asset.AssetRequest;
import com.qio.model.asset.AssetResponse;
import com.qio.model.asset.helper.AssetRequestHelper;
import com.qio.model.assetType.AssetType;
import com.qio.model.tenant.Tenant;
import com.qio.testHelper.AssetTypeTestHelper;
import com.qio.testHelper.TenantTestHelper;
import com.qio.testHelper.TestHelper;

public class CreateAssetsTest extends BaseTestSetupAndTearDown {
	private static AssetTypeTestHelper assetTypeTestHelper;
	private static TenantTestHelper tenantTestHelper;

	private static MAssetAPIHelper assetAPI;
	private AssetRequestHelper assetHelper;
	private AssetRequest requestAsset;
	private AssetResponse responseAsset;

	private static AssetType responseAssetTypePreDef;
	private static String assetTypeId;
	private static Tenant responseTenantPreDef;
	private static String tenantId;

	private static MUserGroupAPIHelper userGroupAPI;

	private ServerResponse serverResp;
	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		baseInitSetupBeforeAllTests("asset");
		assetAPI = new MAssetAPIHelper();

		assetTypeTestHelper = new AssetTypeTestHelper();
		responseAssetTypePreDef = new AssetType();
		responseAssetTypePreDef = assetTypeTestHelper.createAssetType("WithNoAttributesAndParameters");

		String[] assetTypeHrefLinkSplitArray = (responseAssetTypePreDef.get_links().getSelfLink().getHref()).split("/");
		assetTypeId = assetTypeHrefLinkSplitArray[assetTypeHrefLinkSplitArray.length - 1];

		tenantTestHelper = new TenantTestHelper();
		responseTenantPreDef = new Tenant();
		responseTenantPreDef = tenantTestHelper.createTenant();
		tenantId = responseTenantPreDef.getTenantId();
	}

	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// Initializing a new set of objects before each test case.
		assetHelper = new AssetRequestHelper();
		requestAsset = new AssetRequest();
		responseAsset = new AssetResponse();

		serverResp = new ServerResponse();
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1190")) and issue in linkedIssues("RREHM-36")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-629 (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		String abbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space" + abbr);

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Asset Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		// THIS METHOD does not work - we do not need it for this test
		// requestAsset = assetHelper.getAssetCreateDependencies("WithOneAttribute");

		requestAsset.setAbbreviation(TestHelper.FIFTYONE_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Asset  abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation Should not be Empty",
				serverResp);
	}

	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(null);

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation is a required field.",
				serverResp);
	}

	// RREHM-632 (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		String defaultAbbr = requestAsset.getAbbreviation();
		int count = TestHelper.SPECIAL_CHARS.length();

		for (int i = 0; i < count; i++) {
			requestAsset.setAbbreviation(TestHelper.SPECIAL_CHARS.charAt(i) + defaultAbbr);

			serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
					"Asset abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName("");

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name Should not Empty", serverResp);
	}

	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(null);

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name is a required field.",
				serverResp);
	}

	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		requestAsset.setName(TestHelper.TWOFIFTYSIX_CHARS);

		serverResp = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
				"Asset name should be less than 255 characters", serverResp);
	}

	// RREHM-636 (UPDATE)
	// MORE

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	 //RREHM-628 (Asset Abbreviation contains dash, underscore, dot chars)
//	 @Test
//	 public void shouldCreateAssetWithUniqueAbbrWithWhenAbbrContainsDashUnderscoreDot() throws JsonGenerationException, JsonMappingException,
//	 IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//	 requestAsset = assetHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
//	
//	 String abbr = requestAsset.getAbbreviation();
//	 requestAsset.setAbbreviation(abbr+"_-.");
//	
//	 responseAsset = TestHelper.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHeaders,
//	 assetAPI, AssetResponse.class);
//	
//	 // RV1: comparing CreatedObject with CreateRequest, along with response codes.
//	 CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
//	
//	 String assetId = TestHelper.getElementId(responseAsset.get_links().getSelfLink().getHref());
//	 //idsForAllCreatedAssets.add(assetId);
//	
//	 AssetResponse committedAsset = TestHelper.getResponseObjForRetrieve(microservice, environment, assetId,
//	 apiRequestHeaders, assetAPI, AssetResponse.class);
//	 // RV2: comparing CommittedObject with CreatedObject, without the response codes.
//	 CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
//	
//	 }
	/*
	 * POSITIVE TESTS END
	 */

}
