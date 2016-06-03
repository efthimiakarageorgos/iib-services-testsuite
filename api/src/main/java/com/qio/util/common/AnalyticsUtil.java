package com.qio.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsUtil extends BaseTestUtil {
	// There will not be a REST API that will allows us creating and getting info from the "Analytic" collection
	// In order to test the analytic asset map REST API's we will use a predefined analytic
	// and we have to "hard code" its input parameters and its attributes
	//
	public static String analyticIdForAnalyticAssetMapTests = "572bbadb13b38458022a0be6"; // DHP_MechanicalIntegrity
	public static String assetTypeIdForAnalyticAssetMapTests = "572bb56613b38458022725fb";

	// This has to stay here until we have a REST API to create and get an Analytic
	public static Map<String, String> analyticAttributesAndAliasesWithLinks = new HashMap<String, String>() {
		{
			// Analytic Attribute Id Analytic Attribute Alias
			put("572bb56613b3845802272456", "IY01_UpperThreshold");
			put("572bb56613b384580227245a", "IY01_LowerThreshold");
			put("572bb56613b384580227245e", "TI02_UpperThreshold");
			put("572bb56613b3845802272462", "TI02_LowerThreshold");
			put("572bb56613b3845802272466", "VI01_UpperThreshold");
			put("572bb56613b384580227246a", "VI01_LowerThreshold");
			put("572bb56613b384580227246e", "RunState_RuntimeLimit");
			put("572bb56613b3845802272472", "RunState_MotorHertzLimit");
			put("572bb56613b3845802272475", "RunState_Ave3PhaseCurrentLimit");
			put("572bb56613b38458022725a6", "KQ04_Invalid");
			put("572bb56613b38458022725a7", "SI01_Invalid");
			put("572bb56613b38458022725b2", "SI05_Invalid");
			put("572bb56613b38458022725b3", "IY01_Invalid");
			put("572bb56613b38458022725b4", "TI02_Invalid");
			put("572bb56613b38458022725b5", "VI01_Invalid");
			put("572bb56613b38458022725b6", "KQ04_UpperThreshold");
			put("572bb56613b38458022725b7", "KQ04_LowerThreshold");
			put("572bb56613b38458022725b8", "SI01_UpperThreshold");
			put("572bb56613b38458022725b9", "SI01_LowerThreshold");
			put("572bb56613b38458022725ba", "SI05_UpperThreshold");
			put("572bb56613b38458022725bb", "SI05_LowerThreshold");
		}
	};

	// TO DO
	// We want to query the asset type attributes for the assettypeid we have specified above and
	// lookup the analyticAttributesAndAliasesWithLinks based on alias to find the matching attribute
	// then form a new map that contains the analyticAttribute and its matching asset type attribute id
	// the map will look like this:

	public static Map<String, String> analyticAttributesWithLinksMap = new HashMap<String, String>() {
		{
			// Analytic Attribute Id Asset Type Attribute Id
			put("572bb56613b3845802272456", "572bb56613b3845802272647");
			put("572bb56613b384580227245a", "572bb56613b3845802272645");
			put("572bb56613b384580227245e", "572bb56613b384580227260d");
			put("572bb56613b3845802272462", "572bb56613b384580227261a");
			put("572bb56613b3845802272466", "572bb56613b3845802272616");
			put("572bb56613b384580227246a", "572bb56613b384580227264f");
			put("572bb56613b384580227246e", "572bb56613b3845802272656");
			put("572bb56613b3845802272472", "572bb56613b384580227262a");
			put("572bb56613b3845802272475", "572bb56613b3845802272625");
			put("572bb56613b38458022725a6", "572bb56613b3845802272610");
			put("572bb56613b38458022725a7", "572bb56613b384580227260f");
			put("572bb56613b38458022725b2", "572bb56613b384580227260b");
			put("572bb56613b38458022725b3", "572bb56613b3845802272627");
			put("572bb56613b38458022725b4", "572bb56613b3845802272666");
			put("572bb56613b38458022725b5", "572bb56613b384580227265c");
			put("572bb56613b38458022725b6", "572bb56613b3845802272604");
			put("572bb56613b38458022725b7", "572bb56613b3845802272600");
			put("572bb56613b38458022725b8", "572bb56613b3845802272637");
			put("572bb56613b38458022725b9", "572bb56613b384580227262f");
			put("572bb56613b38458022725ba", "572bb56613b384580227264c");
			put("572bb56613b38458022725bb", "572bb56613b3845802272643");
		}
	};

	// This has to stay here until we have a REST API to create and get an Analytic
	public static ArrayList<String> analyticAttributesWithoutLinks = new ArrayList<String>() {
		{
			// These will not be linked to an asset type attribute
			add("572bb56613b38458022724d1");
			add("572bb56613b38458022724cf");
			add("572bb56613b38458022724cc");
			add("572bb56613b38458022724c8");
			add("572bb56613b38458022724c4");
			add("572bb56613b38458022724c0");
			add("572bb56613b38458022724bc");
			add("572bb56613b38458022724b8");
			add("572bb56613b38458022724b4");
			add("572bb56613b38458022724b0");
			add("572bb56613b384580227249f");
			add("572bb56613b384580227249d");
			add("572bb56613b384580227249a");
			add("572bb56613b3845802272496");
			add("572bb56613b3845802272492");
			add("572bb56613b384580227248e");
			add("572bb56613b384580227248a");
			add("572bb56613b3845802272486");
			add("572bb56613b3845802272482");
			add("572bb56613b384580227247e");
			add("572bb56613b3845802272477");
		}
	};

	// This has to stay here until we have a REST API to create and get an Analytic
	public static Map<String, String> analyticInputs = new HashMap<String, String>() {
		{
			// Analytic Input Id Analytic Input Alias
			put("572bb56613b38458022725c6", "LineLeg/GrdUnbalence");
			put("572bb56613b38458022725c8", "DischargePressure");
			put("572bb56613b38458022725ca", "FlowLinePressure");
			put("572bb56613b38458022725cc", "WellHeadPressure");
			put("572bb56613b38458022725ce", "InletPressure");
			put("572bb56613b38458022725d0", "MotorHertz");
			put("572bb56613b38458022725d2", "PumpInletTemperature");
			put("572bb56613b38458022725d3", "PumpMotorTemperature");
			put("572bb56613b38458022725d4", "Vibration");
			put("572bb56613b38458022725d5", "PresentRuntime");
			put("572bb56613b38458022725d6", "Average3PhaseVoltage");
			put("572bb56613b38458022725d7", "Average3PhaseCurrent");
			put("572bb56613b38458022725d8", "PowerFactor");
			put("572bb56613b38458022725d9", "Kilowatt");
			put("572bb56613b38458022725da", "Power");
			put("572bb56613b38458022725db", "ESPTrueLoad");
			put("572bb56613b38458022725dc", "EarthLeakageCurrent");
			put("572bb56613b38458022725dd", "LineVoltUnbalence");
			put("572bb56613b38458022725de", "LineCurrentUnbalance");
			put("572bb56613b38458022725e3", "RuntimeSinceLastStart");
			put("572bb56613b38458022725e5", "TotalNumberofStarts");
			put("572bb56613b38458022725e6", "Calculated_Flow");
			put("572bb56613b38458022725e7", "OilDensity");
			put("572bb56613b38458022725e8", "DriveOutputVoltage");
			put("572bb56613b38458022725e9", "DriveOutputCurrent");
			put("572bb56613b38458022725ea", "Speed");
		}
	};

	// TO DO
	// We want to query the asset type parameters for the assettypeid we have specified above and
	// lookup the analyticInputs based on alias to find the matching parameter - compare to the description field (after spaces are removed)
	// then form a new map that contains the analyticInput and its matching asset type parameter id
	// the map will look like this:

	public static Map<String, String> analyticInputsMap = new HashMap<String, String>() {
		{
			// Analytic Input Id Asset Type Parameter Id
			put("572bb56613b38458022725c6", "56fca9d633c5721c67060707");
			put("572bb56613b38458022725c8", "56fca9d633c5721c67060708");
			put("572bb56613b38458022725ca", "56fca9d633c5721c67060709");
			put("572bb56613b38458022725cc", "56fca9d633c5721c6706070a");
			put("572bb56613b38458022725ce", "56fca9d633c5721c6706070b");
			put("572bb56613b38458022725d0", "56fca9d633c5721c6706070c");
			put("572bb56613b38458022725d2", "56fca9d633c5721c6706070d");
			put("572bb56613b38458022725d3", "56fca9d633c5721c6706070e");
			put("572bb56613b38458022725d4", "56fca9d633c5721c6706070f");
			put("572bb56613b38458022725d5", "56fca9d633c5721c67060710");
			put("572bb56613b38458022725d6", "56fca9d633c5721c67060711");
			put("572bb56613b38458022725d7", "56fca9d633c5721c67060712");
			put("572bb56613b38458022725d8", "56fca9d633c5721c67060713");
			put("572bb56613b38458022725d9", "56fca9d633c5721c67060714");
			put("572bb56613b38458022725da", "56fca9d633c5721c67060715");
			put("572bb56613b38458022725db", "56fca9d633c5721c67060716");
			put("572bb56613b38458022725dc", "56fca9d633c5721c67060717");
			put("572bb56613b38458022725dd", "56fca9d633c5721c67060718");
			put("572bb56613b38458022725de", "56fca9d633c5721c67060719");
			put("572bb56613b38458022725e3", "56fca9d633c5721c6706071a");
			put("572bb56613b38458022725e5", "56fca9d633c5721c6706071b");
			put("572bb56613b38458022725e6", "56fca9d633c5721c67060701");
			put("572bb56613b38458022725e7", "56fca9d633c5721c67060702");
			put("572bb56613b38458022725e8", "56fca9d633c5721c67060703");
			put("572bb56613b38458022725e9", "56fca9d633c5721c67060704");
			put("572bb56613b38458022725ea", "56fca9d633c5721c67060705");
		}
	};

}