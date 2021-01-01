package com.qa.tests;

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

import com.qa.client.restClient;
import com.qa.utils.testUtils;

import restAPI.TestBase;

public class GetAPITest extends TestBase {
	
	TestBase testbase;
	String Envurl;
	String apiurl;
	restClient restclient;
	String url;
	CloseableHttpResponse closeableHttpResponse;
	testUtils utilitobj;
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testbase = new TestBase();
		Envurl = prop.getProperty("url");
		apiurl = prop.getProperty("serviceurl");
		
		url = Envurl + apiurl ;
		
	}
	 
	@Test
	
	public void  getTest() throws ClientProtocolException, IOException {
		
		restclient = new restClient();
		closeableHttpResponse =restclient.get(url);
		
		// Get ststus code
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status coe --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		// Json string
		String Responsestring = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"); 
		JSONObject Responsejson = new JSONObject(Responsestring);
		System.out.println("Response json from API..."+Responsejson);
		
		String  nmlsNumber = utilitobj.getvlueByJpath(Responsejson, "address/city");
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
