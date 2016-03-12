package com.qio.lib.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.qio.lib.apiHelpers.APIHeaders;

public class ConnectionManager {
	
	private static ConnectionManager conManager = null;
	
	// ensures that only one instance of this class exists at all time during the entire run of the tests.
	public static ConnectionManager getInstance() {
		if(conManager == null) {
			conManager = new ConnectionManager();
		}
		return conManager;	
	}
	
	public ConnectionResponse get(String URI, APIHeaders apiHeaders){
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
	
			//add request header
			con.setRequestProperty("Accept", apiHeaders.getAcceptType());
			con.setRequestProperty("Content-Type", apiHeaders.getContentType());
			con.setRequestProperty("X-Auth-Username", apiHeaders.getUserName());
			con.setRequestProperty("X-Auth-Password", apiHeaders.getPassword());
	
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + URI);
			System.out.println("Response Code : " + responseCode);
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			conResp.setRespCode(responseCode);
			conResp.setRespBody(response.toString());
			
			//print result
			System.out.println(response.toString());
			
	//		JsonObject jsonObj = new JsonParser().parse(response.toString()).getAsJsonObject();
	//		System.out.println(jsonObj.getAsJsonObject("_embedded").getAsJsonArray("assettypes").get(0));
	//		JsonElement assetTypeOne = jsonObj.getAsJsonObject("_embedded").getAsJsonArray("assettypes").get(0);
	//		
	//		AssetType assetType1 = mapper.readValue(assetTypeOne.toString(), AssetType.class);
	
	//		AssetType assetType = mapper.readValue("{\"abbreviation\": \"ABBRJeet\","+
	//"\"name\":\"A name\","+
	//"\"description\":\"A Desc\","+
	//"\"attributes\": ["+
	// "{"+
	//"  \"abbreviation\": \"ABBRJeetDogra\","+
	//"  \"name\": \"A Simple Name\","+
	//"  \"description\": \"A Simple Desc\","+
	//"  \"unit\": \"A\","+
	//"  \"datatype\": ["+
	//        "{        \"type\": \"FicticiousDataType\""+
	//        "}"+
	//      "]"+
	//  "}"+
	//  "]"+
	//"}", AssetType.class);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conResp;
	}
	
	public ConnectionResponse post(String URI, String payload, APIHeaders apiHeaders){
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			
			//add request header
			con.setRequestProperty("Accept", apiHeaders.getAcceptType());
			con.setRequestProperty("Content-Type", apiHeaders.getContentType());
			con.setRequestProperty("X-Auth-Username", apiHeaders.getUserName());
			con.setRequestProperty("X-Auth-Password", apiHeaders.getPassword());
	
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(payload);
			wr.flush();
			wr.close();

			System.out.println("\nSending 'POST' request to URL : " + URI);
			System.out.println("Request payload : " + payload);

			int responseCode = con.getResponseCode();
			conResp.setRespCode(responseCode);
			
			System.out.println("Response Code : " + responseCode);
			
			BufferedReader in;
			if(responseCode != 201)
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			else
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			conResp.setRespBody(response.toString());
			
			//print result
			System.out.println("Response Body : " + response.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conResp;
	}

	public ConnectionResponse put(){
		return null;
	}

	public void delete(String URI, APIHeaders apiHeaders){
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			
			//add request header
			con.setRequestProperty("Accept", apiHeaders.getAcceptType());
			con.setRequestProperty("Content-Type", apiHeaders.getContentType());
			con.setRequestProperty("X-Auth-Username", apiHeaders.getUserName());
			con.setRequestProperty("X-Auth-Password", apiHeaders.getPassword());
	
			// Send delete request
			con.setDoOutput(true);
			int responseCode = con.getResponseCode();
			
			System.out.println("\nSending 'DELETE' request to URL : " + URI);
			System.out.println("Response Code : " + responseCode);
	
			conResp.setRespCode(responseCode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
