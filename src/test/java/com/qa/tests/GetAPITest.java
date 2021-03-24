package com.qa.tests;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.client.restClient;
import com.qa.data.users;
import com.qa.utils.testUtils;

import restAPI.TestBase;
import validations.LOdetailsVerification;

public class GetAPITest extends TestBase {
	
	TestBase testbase;
	String Envurl;
	restClient restclient;
	CloseableHttpResponse closeableHttpResponse;
	testUtils utilitobj;
	users users;
	String token;
	String closedloanid ="DOC002526";
	LOdetailsVerification lodetailsVerification;
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testbase = new TestBase();
		Envurl = prop.getProperty("url");
		String url = Envurl + "/api/v1/jwt/auth" ;
		
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		
		ObjectMapper mapper =  new ObjectMapper();
		users =  new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);
		
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		token = utilitobj.getvlueByJpath(jsonObj,"token");
		token = "Bearer "+token;
	}
	 
	@Test(enabled = true)
	
	public void  getTest() throws ClientProtocolException, IOException {
		
		String url = Envurl + "/api/v1/user" ;
		restclient = new restClient();
		
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		// Get ststus code
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status coe --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		// Json string
		String Responsestring = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"); 
		JSONObject Responsejson = new JSONObject(Responsestring);
		
		lodetailsVerification = new LOdetailsVerification();
		lodetailsVerification.responsevalidation(Responsejson);
		
	//			//All Headers
		Header[] headerarray = closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allheaders = new HashMap<String, String>();
		for(Header header : headerarray) {
			allheaders.put(header.getName(), header.getValue());
		}
	//	System.out.println("Header array ... "+ allheaders);
		
	}
	
	@Test(enabled = false)
	public void SubmitCondition() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/conditions/600aabf0b70bb5fbe870a2a4/documentFiles";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "multipart/form-data");
		headermap.put("Authorization", token);
		
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("explanation", "app_user"));
        
		closeableHttpResponse =restclient.postwihformdata(url,urlParameters,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of SubmitCondition --- "+ ststuscode);
		Assert.assertEquals(ststuscode, 204, "status code is nt 204 No Content");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("SubmitCondition endpoin response : "+ jsonObj);
		
	}
	
	@Test(enabled=false)
	public void getLoansByBorrowerUsername() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/test+21.03.2021-11.41.38@gmail.com/loans";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoansByBorrowerUsername --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jarray = new JSONArray(responseString);
		
		JSONObject jsonObj = new JSONObject(jarray);
		System.out.println("Get Profile endpoin response : "+ jarray);				
	}
	
	@Test(enabled=false)
	public void RedirectToSummaryScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/DOC141633";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		ObjectMapper mapper =  new ObjectMapper();
		users =  new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);
		
		
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of RedirectToSummaryScreen --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToSummaryScreen endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get RedirectToSummaryScreen endpoin response : "+ responseString);
		}				
	}
}
