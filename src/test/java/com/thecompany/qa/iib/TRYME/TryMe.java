/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */
package com.thecompany.qa.iib.TRYME;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.thecompany.qa.iib.common.BaseTestSetupAndTearDown;
import com.thecompany.qa.lib.assertions.CustomAssertions;
import com.thecompany.qa.lib.common.MAbstractAPIHelper;
import com.thecompany.qa.lib.exception.ServerResponse;
import com.thecompany.qa.lib.iib.apiHelpers.carsaccounts.MAccountExtensionAPIHelper;
import com.thecompany.qa.lib.iib.apiHelpers.carsaccounts.MAccountsListAPIHelper;

//import com.thecompany.qa.lib.iib.model.accountService.AccountExtension;
//import com.thecompany.qa.lib.iib.model.accountService.AccountList;
import com.thecompany.qa.lib.iib.model.taxService.TaxServiceResponse;
import com.thecompany.qa.lib.iib.model.taxService.helper.TaxServiceHelper;

import com.thecompany.qa.lib.commandLineHelpers.RunCommand;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import java.time.Instant;

import org.junit.*;


import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.junit.rules.TemporaryFolder;


public class TryMe extends BaseTestSetupAndTearDown {

    private static MAccountExtensionAPIHelper accountExtensionAPIRequest;
    private static MAccountsListAPIHelper accountsListAPIRequest;
    private static RunCommand runCom;

    private TaxServiceHelper taxServiceHelper;
    private TaxServiceResponse responseTaxService;

    private ServerResponse serverResp;

    final static Logger logger = Logger.getRootLogger();

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @BeforeClass
    public static void initSetupBeforeAllTests() {
        baseInitSetupBeforeAllTests("primeservicescars");
        accountExtensionAPIRequest = new MAccountExtensionAPIHelper();
        accountsListAPIRequest = new MAccountsListAPIHelper();
        runCom = new RunCommand();
    }

    @Before
    public void initSetupBeforeEveryTest() {
        // Initializing a new set of objects before each test case.
        taxServiceHelper = new TaxServiceHelper();
        responseTaxService = new TaxServiceResponse();
        serverResp = new ServerResponse();
    }

    @AfterClass
    public static void cleanUpAfterAllTests() {
        baseCleanUpAfterAllTests(accountExtensionAPIRequest);
    }

    // Matching test cases in Test Case Management (Jira/Zephyr):
    // issuetype = Test AND issue in (linkedIssues(JIRA-1193)) AND issue in (linkedIssues(JIRA-950), linkedIssues(JIRA-951))


    // JIRA-1268 ()
    @Test
    public void shouldImportLegalEntityAccount() {

        //getAccountList
        //searchBasedOnName and type and locate Id

        String accountId = "1234";
        serverResp = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, accountId, apiHeaderRequestHelper, accountExtensionAPIRequest, ServerResponse.class);
        CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Asset Type id in the URL", serverResp);
    }

    @Test
    public void TRYTHAT() throws InterruptedException {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        DateTimeFormatter parser = ISODateTimeFormat.dateHourMinuteSecondMillis();
        //final DateTime today = new DateTime().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
        //final DateTime today = new DateTime().withZone(DateTimeZone.UTC);
        final DateTime today = new DateTime();
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'SSS")));
// Mon, 29 Feb 2016 23:54:22 -0500
        System.out.println(today.toString(DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z")));

// Today is Monday
        System.out.println(today.toString(DateTimeFormat.forPattern("\'Today is\' EEEE")));

// Tuesday March 01, 2016 is the 61 day of the year
        System.out.println(today.toString(DateTimeFormat.forPattern("EEEE MMMM dd, YYYY \'is the\' D \'day of the year\'")));

// Tuesday March 01, 2016 is the 1 day of the month
        System.out.println(today.toString(DateTimeFormat.forPattern("EEEE MMMM dd, YYYY \'is the\' d \'day of the month\'")));

        System.out.println(Instant.now().toEpochMilli()); //Long = 1450879900184
        System.out.println(Instant.now().getEpochSecond()); //Long = 1450879900

        System.out.println("Epoch Since Millis " + System.currentTimeMillis());


        runCom.RunBatCommand("play.bat christos panagiotis");
        logger.info("BBBB");

        //https://stackoverflow.com/questions/40574892/how-to-send-post-request-with-x-www-form-urlencoded-body/40576153
/*
        String urlParameters  = "param1=data1&param2=data2&param3=data3";
        Sending part now is quite straightforward.

        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        String request = "<Url here>";
        URL url = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
        conn.setUseCaches(false);
        try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write( postData );
        } */

    }

    private static File createTmpFileFromResource(TemporaryFolder folder,
                                                  String classLoaderResource) throws IOException {
        URL resource = Resources.getResource(classLoaderResource);

        File tmpFile = folder.newFile();
        Resources.asByteSource(resource).copyTo(Files.asByteSink(tmpFile));
        return tmpFile;
    }

    @Test
    public void TRYTHISASWELL () throws IOException {

        //@Rule
        //public TemporaryFolder tmpFolder = new TemporaryFolder();

        //File file = createTmpFileFromResource(tmpFolder, "file.txt");
        File goodFile = createTmpFileFromResource(tmpFolder, "CashOneToCARSInputData/File1.csv");
        goodFile.getAbsoluteFile();

        FileReader fr = new FileReader(goodFile);
        String s;
        try {
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                s.replaceAll("AUTOMATION1", "AUTOMATION1-1234");
                System.out.print(s);
                // do something with the resulting line
            }
        } catch (IOException e){}

        logger.info("ENA DUO TRIA");
        InputStream is = null;
        int i;
        char c;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try {
            // new input stream created
            is = classloader.getResourceAsStream("\\CashOneToCARSInputData\\File1.csv");
            logger.info("Characters printed:");
            System.out.println("Characters printed:");


            // reads till the end of the stream
            String line = "", oldtext = "";
            while((i = is.read())!=-1) {
                // converts integer to character
                c = (char)i;
                // prints character
                System.out.print(c);
                oldtext += c + "\r\n";
            }
            logger.info("OL  "+oldtext);

        } catch(Exception e) {
            // if any I/O error occurs
            e.printStackTrace();
        } finally {
            // releases system resources associated with this stream
            if(is!=null)
                is.close();
        }

        // Write updated record to a file
        //FileWriter writer = new FileWriter("outputfile.txt");
        //writer.write(newText);
        //writer.close();
    }

    @Test
    public void TRYTHIS() {
        //https://hc.apache.org/httpcomponents-client-ga/tutorial/html/authentication.html

        /*
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new NTCredentials("user", "pwd", "myworkstation", "microsoft.com"));

        HttpHost target = new HttpHost("www.microsoft.com", 80, "http");

        // Make sure the same context is used to execute logically related requests
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);

        // Execute a cheap method first. This will trigger NTLM authentication
        HttpGet httpget = new HttpGet("/ntlm-protected/info");
        CloseableHttpResponse response1 = httpclient.execute(target, httpget, context);
        try {
            HttpEntity entity1 = response1.getEntity();
        } finally {
            response1.close();
        }

// Execute an expensive method next reusing the same context (and connection)
        HttpPost httppost = new HttpPost("/ntlm-protected/form");
        httppost.setEntity(new StringEntity("lots and lots of data"));
        CloseableHttpResponse response2 = httpclient.execute(target, httppost, context);
        try {
            HttpEntity entity2 = response2.getEntity();
        } finally {
            response2.close();
        }
        */

        String accountId = "ThisAssetTypeDoesNotExist";
        //String TRYME = MAbstractAPIHelper.getJSONResponseForRetrieve(microservice, environment, accountId, apiHeaderRequestHelper, accountExtensionAPIRequest);

        String TRYME = MAbstractAPIHelper.getJSONResponseForRetrieveAll(microservice, environment, apiHeaderRequestHelper, accountsListAPIRequest);
logger.info("ssss   " + TRYME);
        //CustomAssertions.assertServerError(404, "com.thecompany.application.exceptions.InvalidParameterException", "Wrong Account id in the URL", serverResp);
    }
}
