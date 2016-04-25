package com.qio.lib.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.qio.lib.apiHelpers.APIHeaders;

public class ConnectionManager {
	
	private static ConnectionManager conManager = null;
	final static Logger logger = Logger.getLogger(ConnectionManager.class);
	
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
			logger.debug("Sending 'GET' request to URL : " + URI);
	
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
			logger.debug("Response Code and Body: " + conResp.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
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

			logger.debug("Sending 'POST' request to URL : " + URI);
			logger.debug("Request payload : " + payload);

			int responseCode = con.getResponseCode();
			conResp.setRespCode(responseCode);
			
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
			logger.debug("Response Code and Body: " + conResp.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return conResp;
	}

	public ConnectionResponse put(String URI, String payload, APIHeaders apiHeaders) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");

			// add request header
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

			logger.debug("Sending 'PUT' request to URL : " + URI);
			logger.debug("Request payload : " + payload);

			int responseCode = con.getResponseCode();
			conResp.setRespCode(responseCode);

			BufferedReader in;
			if (responseCode != 200)
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

			// print result
			logger.debug("Response Code and Body: " + conResp.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return conResp;
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
			
			logger.debug("Sending 'DELETE' request to URL : " + URI);
			logger.debug("Response Code and Body: " + conResp.toString());
	
			conResp.setRespCode(responseCode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

}
