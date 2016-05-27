package com.qio.lib.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.BaseHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConnectionManager {

	private static ConnectionManager conManager = null;
	private OauthValidationResponse oauthValidationResponse = null;
	final static Logger logger = Logger.getLogger(ConnectionManager.class);

	// ensures that only one instance of this class exists at all time during
	// the entire run of the tests.
	public static ConnectionManager getInstance() {
		if (conManager == null) {
			conManager = new ConnectionManager();
		}
		return conManager;
	}

	public ConnectionResponse get(String URI, APIRequestHelper apiRequestHelper) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Accept", apiRequestHelper.getAcceptType());
			con.setRequestProperty("Content-Type", apiRequestHelper.getContentType());
			con.setRequestProperty("Authorization", oauthValidationResponse.getToken_type() + " " + oauthValidationResponse.getAccess_token());

			logger.debug("Sending 'GET' request to URL : " + URI);

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

	public ConnectionResponse post(String URI, String payload, APIRequestHelper apiRequestHelper) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			// add request header
			con.setRequestProperty("Accept", apiRequestHelper.getAcceptType());
			con.setRequestProperty("Content-Type", apiRequestHelper.getContentType());
			con.setRequestProperty("Authorization", oauthValidationResponse.getToken_type() + " " + oauthValidationResponse.getAccess_token());

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
			//AnalyticAssetMap POST returns 200
			if (responseCode != 201 && responseCode != 200)
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

	public ConnectionResponse put(String URI, String payload, APIRequestHelper apiRequestHelper) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");

			// add request header
			con.setRequestProperty("Accept", apiRequestHelper.getAcceptType());
			con.setRequestProperty("Content-Type", apiRequestHelper.getContentType());
			con.setRequestProperty("Authorization", oauthValidationResponse.getToken_type() + " " + oauthValidationResponse.getAccess_token());

			// Send put request
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

	public void delete(String URI, APIRequestHelper apiRequestHelper) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");

			// add request header
			con.setRequestProperty("Accept", apiRequestHelper.getAcceptType());
			con.setRequestProperty("Content-Type", apiRequestHelper.getContentType());
			con.setRequestProperty("Authorization", oauthValidationResponse.getToken_type() + " " + oauthValidationResponse.getAccess_token());

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

	public void initOauthAccessToken(String URI, APIRequestHelper apiRequestHelper) {
		/*
		 * Fetching new access token under the following scenarios: 1. if this is the first time we are going to fetch the access token. 2. if a new
		 * object for APIHeaders has been instantiated, or its user/password has been changed(upon which the flag fetchNewAccessToken will be set to
		 * True), or we explicitly set the flag fetchNewAccessToken to true. 3. if the access token has expired
		 * 
		 * TODO: Include the condition for expiry of the access token, although at present its set to 130min, but just in case its reduced and our
		 * tests take longer to run then there can be a problem.
		 */
		try {
			if (oauthValidationResponse == null || apiRequestHelper.getFetchNewAccessToken()) {
				Config applicationUserConfig = ConfigFactory.load("application_user_creds.conf");
				String basicAuthStr = "Basic " + Base64.getEncoder().encodeToString((applicationUserConfig.getString("application_user.username")
						+ ":" + applicationUserConfig.getString("application_user.password")).getBytes());

				ConnectionResponse conResp = getOauthValidationResponse(URI, basicAuthStr, apiRequestHelper);

				oauthValidationResponse = BaseHelper.toClassObject(conResp.getRespBody(), OauthValidationResponse.class);
				apiRequestHelper.setFetchNewAccessToken(false);
			}
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	private ConnectionResponse getOauthValidationResponse(String URI, String basicAuthStr, APIRequestHelper apiRequestHelper) {
		ConnectionResponse conResp = new ConnectionResponse();
		URL url;
		try {
			url = new URL(URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			StringBuilder postParams = new StringBuilder();
			postParams.append("username=" + apiRequestHelper.getUserName());
			postParams.append("&password=" + apiRequestHelper.getPassword());
			postParams.append("&grant_type=" + apiRequestHelper.getGrant_type());
			postParams.append("&scope=" + apiRequestHelper.getScope());

			byte[] postData = postParams.toString().getBytes(StandardCharsets.UTF_8);

			// add request header
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("charset", "utf-8");
			con.setRequestProperty("Content-Length", Integer.toString(postData.length));
			con.setRequestProperty("Authorization", basicAuthStr);

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			// wr.writeBytes(postParams);
			wr.write(postData);
			wr.flush();
			wr.close();

			logger.debug("Sending 'POST' request to URL : " + URI);

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
}
