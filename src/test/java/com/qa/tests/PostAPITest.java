package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.client.restClient;
import com.qa.data.users;
import com.qa.utils.testUtils;

import restAPI.TestBase;

public class PostAPITest extends TestBase{

	
	TestBase testbase;
	String Envurl;
	String apiurl;
	restClient restclient;
	String url;
	CloseableHttpResponse closeableHttpResponse;
	testUtils utilitobj;
	users users;
	String JsonFilePath = "F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json";
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testbase = new TestBase();
		Envurl = prop.getProperty("url");
		apiurl = prop.getProperty("serviceurl");
		
		url = Envurl + apiurl ;
		
	}
	
	@Test
	public void postAPITest() throws JsonGenerationException, JsonMappingException, IOException {
		
		restclient = new restClient();
		
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		
		ObjectMapper mapper =  new ObjectMapper();
		users =  new users("naf","docittlender@gmail.com","Jan@2017");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		
		String UserJsonString = mapper.writeValueAsString(users);
		
		System.out.println(UserJsonString);
		
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		
		Assert.assertEquals(ststuscode,STATUS_RESPONSE_CODE_200);
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("respinse in Json.. "+jsonObj);
		
		String  token = utilitobj.getvlueByJpath(jsonObj,"token");
		System.out.println("token is "+token);
		
	}
	
}
