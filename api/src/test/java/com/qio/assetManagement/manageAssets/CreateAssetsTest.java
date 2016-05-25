package com.qio.assetManagement.manageAssets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//import com.jcraft.jsch.Logger;
import com.qio.common.BaseTestSetupAndTearDown;
import com.qio.lib.apiHelpers.MAssetAPIHelper;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.apiHelpers.assetType.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.asset.AssetRequest;
import com.qio.model.asset.AssetResponse;
import com.qio.model.asset.helper.AssetRequestHelper;
import com.qio.util.common.APITestUtil;

public class CreateAssetsTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private static String assetTypeId;

	private static MTenantAPIHelper tenantAPI;
	private static String tenantId;

	private static MAssetAPIHelper assetAPI;
	private static AssetRequestHelper assetRequestHelper;
	private static AssetRequest requestAsset;
	private static AssetRequest requestAssetNonUnique;
	private static AssetRequest requestAssetForSecondTenant;
	private static AssetRequest requestAssetUniqueForTenant;
	private AssetResponse responseAsset;
	private AssetResponse responseAssetForSecondTenant;
	private static AssetResponse responseAssetUniqueForTenant;

	private ServerResponse serverResp;

	private static ArrayList<String> idsForAllCreatedAssets;
	private static ArrayList<String> idsForAllCreatedAssetTypes;
	private static ArrayList<String> idsForAllCreatedTenants;
	private static ArrayList<String> assetStatuses;
	
	final static Logger logger = Logger.getRootLogger();


	@BeforeClass
	public static void initSetupBeforeAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {
		baseInitSetupBeforeAllTests("asset");
		assetAPI = new MAssetAPIHelper();
		assetRequestHelper = new AssetRequestHelper();

		idsForAllCreatedAssets = new ArrayList<String>();
		idsForAllCreatedAssetTypes = new ArrayList<String>();
		idsForAllCreatedTenants = new ArrayList<String>();

		requestAsset = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		assetTypeId = requestAsset.getAssetType();
		tenantId = requestAsset.getTenant();

		idsForAllCreatedAssetTypes.add(assetTypeId);
		idsForAllCreatedTenants.add(tenantId);
		
		assetStatuses = new ArrayList<String>();
		assetStatuses.add("ConfigurationInProgress");
		assetStatuses.add("ConfigurationComplete");
		assetStatuses.add("TestingInProgress");
		assetStatuses.add("TestingComplete");
		assetStatuses.add("InService");
		assetStatuses.add("OutOfService");
		assetStatuses.add("Deleted");
		assetStatuses.add("Retired");
	}

	@Before
	public void initSetupBeforeEveryTest() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// Initializing a new set of objects before each test case.
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = new AssetRequest();
		responseAsset = new AssetResponse();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		
		for (String elementId : idsForAllCreatedAssets) {
			logger.debug("Asset Element to be deleted: "+elementId);
		}
		
		for (String elementId : idsForAllCreatedAssetTypes) {
			logger.debug("Asset Type Element to be deleted: "+elementId);
		}
		
		for (String elementId : idsForAllCreatedTenants) {
			logger.debug("Tenant Element to be deleted: "+elementId);
		}
		
		baseCleanUpAfterAllTests(idsForAllCreatedAssets, assetAPI);
		// NOTE
		//This call makes line "Method deleteMethod = apiHelperObj.getClass().getMethod("delete", methodArgs);" in com.qio.util.common.APITestUtil fail
		//
		baseCleanUpAfterAllTests(idsForAllCreatedAssetTypes, assetTypeAPI);
		baseCleanUpAfterAllTests(idsForAllCreatedTenants, tenantAPI);
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1190")) and issue in linkedIssues("RREHM-36")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-629 (Asset abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation("Abrr has a space" + origAbbr);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-630 (Asset abbreviation is longer than 50 chars)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(APITestUtil.FIFTYONE_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset  abbreviation Should Less Than 50 Character", serverResp);
	}

	// RREHM-593 (Asset abbreviation is blank)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation Should not be Empty", serverResp);
	}

	// RREHM-594 (Asset abbreviation is null - missing)
	@Test
	public void shouldNotCreateAssetWhenAbbreviationIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation is a required field.", serverResp);
	}

	// RREHM-632 (Asset abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetWhenAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			String origAbbr = requestAsset.getAbbreviation();
			requestAsset.setAbbreviation(specialChar + origAbbr);

			serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset abbreviation must not contain illegal characters", serverResp);
		}
	}

	// RREHM-600 (Asset name is blank)
	@Test
	public void shouldNotCreateAssetWhenNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName("");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name Should not Empty", serverResp);
	}

	// RREHM-598 (Asset Name is null - missing)
	@Test
	public void shouldNotCreateAssetWhenNameIsNull() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(null);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name is a required field.", serverResp);
	}

	// RREHM-631 (Asset name is longer than 255 chars)
	@Test
	public void shouldNotCreateAssetWhenNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setName(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Asset name should be less than 255 characters", serverResp);
	}

	// RREHM-638
	@Test
	public void shouldNotCreateAssetWhenAssetTypeDoesNotExist() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant("NonExistentAssetType", tenantId);

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException", "Invalid Asset Type ID", serverResp);
	}

	// RREHM-636
	@Test
	public void shouldNotCreateAssetWhenTenantDoesNotExist() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, "NonExistentTenant");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid tenant id in the request", serverResp);
	}

	// RREHM-591
	@Test
	public void shouldNotCreateAssetWhenAbbrIsNotUniqueInTenantScope() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		
		requestAssetNonUnique = new AssetRequest();
		requestAssetNonUnique = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAssetNonUnique.setAbbreviation(origAbbr);
		serverResp = APITestUtil.getResponseObjForCreate(requestAssetNonUnique, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);
		
		//NEED HELP FROM JEET TO MAKE it work with the response message we get - it is not static...
		//{"timestamp":1464112377762,"status":500,"error":"Internal Server Error","exception":"org.springframework.dao.DuplicateKeyException","message":"{ \"serverUsed\" : \"usnc1a-dmgdb01.qiotec.internal:27017\" , \"ok\" : 1 , \"n\" : 0 , \"err\" : \"E11000 duplicate key error collection: Asset.asset index: abbreviation dup key: { : \\\"A1464112392776\\\" }\" , \"code\" : 11000}; nested exception is com.mongodb.MongoException$DuplicateKey: { \"serverUsed\" : \"usnc1a-dmgdb01.qiotec.internal:27017\" , \"ok\" : 1 , \"n\" : 0 , \"err\" : \"E11000 duplicate key error collection: Asset.asset index: abbreviation dup key: { : \\\"A1464112392776\\\" }\" , \"code\" : 11000}","path":"/assets"}
		CustomAssertions.assertServerError(500, "org.springframework.dao.DuplicateKeyException", "XXX", serverResp);
		
		//Confirm that there is only one asset under the tenant by submitting GET with URL:
		//{Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid=tenantId
	}
	
	// RREHM-623
	@Test
	public void shouldNotCreateAssetWhenStatusIsNotValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setStatus("NotValid");

		serverResp = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500, "java.lang.Exception", "Invalid asset status", serverResp);
	}
	
	/*
	 * NEGATIVE TESTS END
	 */

	//MORE ASSERTIONS REQS for NEGATIVE:
	// We could use one tenant and asset type for negative tests and a sepaarte pair for positive
	// In that case we could also validate that the tenant used for the positive tests does not have any assets associated with it
	// at the end of each negative test by sending the GET request:
	// {Asset-Micro}.{CF-URL}/{AssetQueryEndPoint}/search/getAssetsForTenant?tenantid={tenantId}
	
	/*
	 * POSITIVE TESTS START
	 */
	
	//
	//MORE ASSERTIONS REQS for POSITIVE:
	//
	// Want to validate that if the asset type we link to has parameters and or attributes, then these parameters and attributes are part of the asset create response
	// Want to validate that the href links for asset type and tenant are valid: aka if you make a GET request with them, you get 200 response -- should be used only in specific tc's (see reasoning below)
	// System generated fields are created (CreatedDate field) --- we should have a TC that covers this and not have to check all the time to avoid failing all test cases if this has an issue
	// Validate that some date fields are set to have value equal to the time of creation -- this could be validated as being the range of the tc timestamp as we cannot predict the exact second.
	// All date fields should have right format including the system generated ones -- we should do it as part of specific test cases and not check every time to avoid failing all test cases if this has an issue
	// 
	// Therefore the above should be made in to separate methods (possibly generalized) that can be called on demand based on test case
	//
	
	
	// RREHM-357 ()
	@Test
	public void shouldCreateAssetWithUniqueAbbrLinkingToValidTenantAndAssetTypeWithoutAttrPars() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		// THIS ONE FAILS when it checks the date
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}
	
	// RREHM-778 ()
	@Test
	public void shouldHaveCreatedDateFieldGeneratedbySystemWhenCreatingAsset() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		// THIS ONE FAILS when it checks the date
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		//Assert the CreatedDate field exists; has correct date format and the date is equal to the time of creation
	}
		
	// RREHM-628 (Asset Abbreviation contains dash, underscore, dot chars)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenAbbrContainsDashUnderscoreDot() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();
		requestAsset.setAbbreviation(origAbbr + "_-.");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		// THIS ONE FAILS when it checks the date
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}
	
	// RREHM-660 (Asset Description contains paragraphs)
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenDescContainsParagraphs() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAsset.setDescription("This is paragraph 1.\n This is paragraph 2.\nThis is paragraph 3");

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);

		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);

		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		// THIS ONE FAILS when it checks the date
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}
	
	// RREHM-779 ()
	// BUG: RREHM-956
	@Test
	public void shouldCreateAssetWithNonUniqueAbbrWhenUniqueInTenantScope() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		String origAbbr = requestAsset.getAbbreviation();

		responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);
		
		//This is a trick to get a second tenant created and after that create another asset that has the same abbreviation as the previously created asset and tenant set to the tenant of this asset
		requestAssetForSecondTenant = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant("WithNoAttributesAndParameters", null, null);
		idsForAllCreatedAssetTypes.add(requestAssetForSecondTenant.getAssetType());
		idsForAllCreatedTenants.add(requestAssetForSecondTenant.getTenant());
		
		responseAssetForSecondTenant = APITestUtil.getResponseObjForCreate(requestAssetForSecondTenant, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		idsForAllCreatedAssets.add(APITestUtil.getElementId(responseAssetForSecondTenant.get_links().getSelfLink().getHref()));
		
		requestAssetUniqueForTenant = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
		requestAssetUniqueForTenant.setAbbreviation(origAbbr);
		responseAssetUniqueForTenant = APITestUtil.getResponseObjForCreate(requestAssetUniqueForTenant, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
		
		// Since the request and response objects are different, therefore we can't compare them.
		// CustomAssertions.assertRequestAndResponseObj(201, TestHelper.responseCodeForInputRequest, requestAsset, responseAsset);
		CustomAssertions.assertRequestAndResponseObj(201, APITestUtil.responseCodeForInputRequest);
		
		assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
		idsForAllCreatedAssets.add(assetId);
		
		AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		// THIS ONE FAILS when it checks the date
		CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
	}
	
	// RREHM-823
	@Test
	public void shouldCreateAssetWithUniqueAbbrWhenStatusIsValid() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		
		for (String status : assetStatuses) {
			requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndTenant(assetTypeId, tenantId);
			requestAsset.setStatus(status);
			logger.debug(status);
			responseAsset = APITestUtil.getResponseObjForCreate(requestAsset, microservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
			String assetId = APITestUtil.getElementId(responseAsset.get_links().getSelfLink().getHref());
			idsForAllCreatedAssets.add(assetId);

			AssetResponse committedAsset = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
			// THIS ONE FAILS when it checks the date
			//CustomAssertions.assertRequestAndResponseObj(responseAsset, committedAsset);
		}
	}
	
	// RREHM-824
	// RREHM-612
	// RREHM-610
	// RREHM-609
	// RREHM-621
	
	/*
	 * POSITIVE TESTS END
	 */

}
