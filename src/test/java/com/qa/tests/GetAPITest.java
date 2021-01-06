package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.client.restClient;
import com.qa.data.users;
import com.qa.utils.testUtils;

import restAPI.TestBase;

public class GetAPITest extends TestBase {
	
	TestBase testbase;
	String Envurl;
	restClient restclient;
	CloseableHttpResponse closeableHttpResponse;
	testUtils utilitobj;
	users users;
	String token;
	
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
	 
	@Test
	
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
		System.out.println("Response json from API..."+Responsejson);
		
		String  nmlsNumber = utilitobj.getvlueByJpath(Responsejson, "nmlsNumber");
		System.out.println("nmlsNumber is "+nmlsNumber);
		
		//All Headers
		Header[] headerarray = closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allheaders = new HashMap<String, String>();
		for(Header header : headerarray) {
			allheaders.put(header.getName(), header.getValue());
		}
		System.out.println("Header array ... "+ allheaders);
		
	}

}
