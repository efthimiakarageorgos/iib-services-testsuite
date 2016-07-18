package com.qio.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsUtil extends BaseTestUtil {
	// There will not be a REST API that will allows us creating and getting info from the "Analytic" collection
	// In order to test the analytic asset map REST API's we will use a predefined analytic
	// and we have to "hard code" its input parameters and its attributes
	//
	public static String analyticIdForAnalyticAssetMapTests = "5720c00fb38fcd4e5f2cff66"; // DHP_MechanicalIntegrity
	public static String assetTypeIdForAnalyticAssetMapTests = "5720babbb38fcd4e5f2a197b"; //"572bb56613b38458022725fb"

	// This has to stay here until we have a REST API to create and get an Analytic
	public static Map<String, String> analyticAttributesAndAliasesWithLinks = new HashMap<String, String>() {
		{
			// Analytic Attribute Id    -   Asset Type Attribute Name // Analytic Attribute Alias
			put("5720babbb38fcd4e5f2a17d6", "IY01_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a17da", "IY01_LowerThreshold");  //Same
			
			put("5720babbb38fcd4e5f2a17de", "TI02_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a17e2", "TI02_LowerThreshold");  //Same
			
			put("5720babbb38fcd4e5f2a17e6", "VI01_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a17ea", "VI01_LowerThreshold");  //Same
			
			put("5720babbb38fcd4e5f2a17ee", "RunState_RuntimeLimit"); //Same
			put("5720babbb38fcd4e5f2a17f2", "RunState_MotorHertzLimit"); //Same
			put("5720babbb38fcd4e5f2a17f5", "RunState_Ave3PhaseCurrentLimit"); // Same
			
			put("5720babbb38fcd4e5f2a1926", "KQ04_Invalid");  //Same
			put("5720babbb38fcd4e5f2a1927", "SI01_Invalid");  //Same
			put("5720babbb38fcd4e5f2a1932", "SI05_Invalid");  //Same
			put("5720babbb38fcd4e5f2a1933", "IY01_Invalid");  //Same
			put("5720babbb38fcd4e5f2a1934", "TI02_Invalid");  //Same
			put("5720babbb38fcd4e5f2a1935", "VI01_Invalid");  //Same
			
			put("5720babbb38fcd4e5f2a1936", "KQ04_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a1937", "KQ04_LowerThreshold");  //Same
			put("5720babbb38fcd4e5f2a1938", "SI01_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a1939", "SI01_LowerThreshold");  //Same
			put("5720babbb38fcd4e5f2a193a", "SI05_UpperThreshold");  //Same
			put("5720babbb38fcd4e5f2a193b", "SI05_LowerThreshold");  //Same
			
			put("5720babbb38fcd4e5f2a17fe", "Pump Temperature Upper Warning Limit"); //"Temp_U1_Limit"
			put("5720babbb38fcd4e5f2a1802", "Pump Temperature Upper Warning Hysteresis"); //"Temp_U1_Hysteresis"
			
			put("5720babbb38fcd4e5f2a180a", "Pump Temperature Upper Critical Limit"); //"Temp_U2_Limit"
			put("5720babbb38fcd4e5f2a180e", "Pump Temperature Upper Critical Hysteresis "); //"Temp_U2_Hysteresis"
		}
	};
	      
	 
	// TO DO
	// We want to query the asset type attributes for the assettypeid we have specified above and
	// lookup the analyticAttributesAndAliasesWithLinks based on alias to find the matching attribute 
	// based on attribute name
	// then form a new map that contains the analyticAttribute and its matching asset type attribute id
	// the map will look like this:

	public static Map<String, String> analyticAttributesWithLinksMap = new HashMap<String, String>() {
		{
			// Analytic Attribute Id     -   Asset Type Attribute Id
			put("5720babbb38fcd4e5f2a17d6", "5720babbb38fcd4e5f2a19c7");
			put("5720babbb38fcd4e5f2a17da", "5720babbb38fcd4e5f2a19c5");
			
			put("5720babbb38fcd4e5f2a17de", "5720babbb38fcd4e5f2a198d");
			put("5720babbb38fcd4e5f2a17e2", "5720babbb38fcd4e5f2a199a");
			
			put("5720babbb38fcd4e5f2a17e6", "5720babbb38fcd4e5f2a17e6");
			put("5720babbb38fcd4e5f2a17ea", "5720babbb38fcd4e5f2a17ea");
			
			put("5720babbb38fcd4e5f2a17ee", "5720babbb38fcd4e5f2a19d6");
			put("5720babbb38fcd4e5f2a17f2", "5720babbb38fcd4e5f2a19aa");
			put("5720babbb38fcd4e5f2a17f5", "5720babbb38fcd4e5f2a19a5");
			
			put("5720babbb38fcd4e5f2a1926", "5720babbb38fcd4e5f2a1990");
			put("5720babbb38fcd4e5f2a1927", "5720babbb38fcd4e5f2a198f");
			put("5720babbb38fcd4e5f2a1932", "5720babbb38fcd4e5f2a198b");
			put("5720babbb38fcd4e5f2a1933", "5720babbb38fcd4e5f2a19a7");
			put("5720babbb38fcd4e5f2a1934", "5720babbb38fcd4e5f2a19e6");
			put("5720babbb38fcd4e5f2a1935", "5720babbb38fcd4e5f2a19dc");
			
			put("5720babbb38fcd4e5f2a1936", "5720babbb38fcd4e5f2a1984");
			put("5720babbb38fcd4e5f2a1937", "5720babbb38fcd4e5f2a1980");
			put("5720babbb38fcd4e5f2a1938", "5720babbb38fcd4e5f2a19b7");
			put("5720babbb38fcd4e5f2a1939", "5720babbb38fcd4e5f2a19af");
			put("5720babbb38fcd4e5f2a193a", "5720babbb38fcd4e5f2a19cc");
			put("5720babbb38fcd4e5f2a193b", "5720babbb38fcd4e5f2a19c3");
			
			put("5720babbb38fcd4e5f2a17fe", "575ee18c3386ae2ea39f3758");
			put("5720babbb38fcd4e5f2a1802", "575ee18c3386ae2ea39f3759");
			
			put("5720babbb38fcd4e5f2a180a", "575ee18d3386ae2ea39f375a");
			put("5720babbb38fcd4e5f2a180e", "575ee18d3386ae2ea39f375b");
		}
	};

	// This has to stay here until we have a REST API to create and get an Analytic
	public static ArrayList<String> analyticAttributesWithoutLinks = new ArrayList<String>() {
		{
			// These will not be linked to an asset type attribute
			add("5720babbb38fcd4e5f2a17f7"); //MaxHistoryCount, "200"
			add("5720babbb38fcd4e5f2a1806"); //"Temp_U1_CounterLimit", "12"
			add("5720babbb38fcd4e5f2a1812"); //"Temp_U2_CounterLimit", "12"
			add("5720babbb38fcd4e5f2a1816"); //"Temp_EWMA_ForgettingFactor","0.03"   
			add("5720babbb38fcd4e5f2a181a"); // "Temp_EWMA_Weight", "0.05"
			add("5720babbb38fcd4e5f2a181d"); // "Temp_EWMA_Threshold", "4.5"
			add("5720babbb38fcd4e5f2a181f"); // "Temp_EWMA_MagLimit","2"
			add("5720babbb38fcd4e5f2a1830");// "Vib_U1_Limit", "0.75"
			add("5720babbb38fcd4e5f2a1834");// "Vib_U1_Hysteresis", "0.65"
			add("5720babbb38fcd4e5f2a1838");// "Vib_U1_CounterLimit","6"
			add("5720babbb38fcd4e5f2a183c");// "Vib_U2_Limit", "1"
			add("5720babbb38fcd4e5f2a1840");// "Vib_U2_Hysteresis", "0.8"
			add("5720babbb38fcd4e5f2a1844");// "Vib_U2_CounterLimit","6"
			add("5720babbb38fcd4e5f2a1848");// "Vib_EWMA_ForgettingFactor","0.05"
			add("5720babbb38fcd4e5f2a184c");// "Vib_EWMA_Weight", "0.03"
			add("5720babbb38fcd4e5f2a184f"); // "Vib_EWMA_Threshold","4"
			add("5720babbb38fcd4e5f2a1851"); // "Vib_EWMA_MagLimit", "0.05"
		}
	};

	// This has to stay here until we have a REST API to create and get an Analytic
	public static Map<String, String> analyticInputs = new HashMap<String, String>() {
		{
			// Analytic Input Id Analytic Input Alias
			put("5720babbb38fcd4e5f2a196b", "Runtime Since Last Start"); //"RuntimeFromLastStart"
			put("5720babbb38fcd4e5f2a196c", "Motor Hertz"); //MotorHertz
			put("5720babbb38fcd4e5f2a196d", "Speed"); //Same
			put("5720babbb38fcd4e5f2a196e", "Average 3 Phase Current"); //"Ave3PhaseCurrent"
			put("5720babbb38fcd4e5f2a196f", "Pump Motor Temperature");  //"PumpMotorTemp"
			put("5720babbb38fcd4e5f2a1970", "Vibration"); //"PumpVibration"
		}
	};

	// TO DO
	// We want to query the asset type parameters for the assettypeid we have specified above and
	// lookup the analyticInputs based on alias to find the matching parameter - compare to the description field (after spaces are removed)
	// then form a new map that contains the analyticInput and its matching asset type parameter id
	// the map will look like this:

	public static Map<String, String> analyticInputsMap = new HashMap<String, String>() {
		{
			// Analytic Input Id          - Asset Type Parameter Id
			put("5720babbb38fcd4e5f2a196b", "56fca9d633c5721c6706071a");
			put("5720babbb38fcd4e5f2a196c", "56fca9d633c5721c6706070c");
			put("5720babbb38fcd4e5f2a196d", "56fca9d633c5721c67060705");
			put("5720babbb38fcd4e5f2a196e", "56fca9d633c5721c67060712");
			put("5720babbb38fcd4e5f2a196f", "56fca9d633c5721c6706070e");
			put("5720babbb38fcd4e5f2a1970", "56fca9d633c5721c6706070f");
		}
	};

}