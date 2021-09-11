package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.client.restClient;
import com.qa.data.CreateLoansUsingInvitationToken;
import com.qa.data.InviteUserData;
import com.qa.data.users;
import com.qa.utils.testUtils;

import TestData.DeclarationData;
import TestData.IncomeData;
import TestData.ProfileData;
import restAPI.TestBase;
import validations.LOdetailsVerification;

public class SITWorkflow extends TestBase {

	TestBase testbase;
	String Envurl;
	restClient restclient;
	CloseableHttpResponse closeableHttpResponse;
	testUtils utilitobj;
	users users;
	String token;
	String invitationUrl;
	String invitationToken;
	String email;
	String loanid;
	String ProfilejobId;
	String incomejobId;
	String declarationjobId;
	String closedloanid = "DOC002204";
	String DocumetnID;
	ProfileData Profiledata;
	
	@BeforeTest
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
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		token = utilitobj.getvlueByJpath(jsonObj,"token");
		token = "Bearer "+token;
	}
	
	@Test(priority=1, enabled= true)
	public void inviteBorrower() throws JsonGenerationException, JsonMappingException, IOException {
		
		String url = Envurl + "/api/v1/borrower/invite" ;
		
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		ObjectMapper mapper =  new ObjectMapper();
		email =testUtils.generateRandomEmail();
		InviteUserData inviteClass =  new InviteUserData("Bhagat","Kher",email,"9898016454");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), inviteClass);
		String UserJsonString = mapper.writeValueAsString(inviteClass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		}else {
			System.out.println(jsonObj);
		}
		
		invitationUrl = utilitobj.getvlueByJpath(jsonObj,"invitationUrl");
		invitationToken = utilitobj.getvlueByJpath(jsonObj,"invitationToken");
		
	}
	
	@Test(priority=2, enabled= true)
	public void CreateBorrower() throws JsonGenerationException, JsonMappingException, IOException {
		
		String url = Envurl + "/api/v2/loans/create" ;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		ObjectMapper mapper =  new ObjectMapper();
		CreateLoansUsingInvitationToken createuserclass =  new CreateLoansUsingInvitationToken("EnumTransactionTypePurchase",invitationToken,email,"Docabc@123");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), createuserclass);
		String UserJsonString = mapper.writeValueAsString(createuserclass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of CreateBorrower --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		}else {
			System.out.println(jsonObj);
		}
		
		loanid = utilitobj.getvlueByJpath(jsonObj,"loanId");
		System.out.println("Loan id " + loanid);
		
	}
	
	@Test(priority=3,enabled =true)
	public void updateprofiledata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/profile";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		ProfileData PD = new ProfileData();
		String ProfilePayload = PD.ProfilePayload(email);
		
		closeableHttpResponse =restclient.put(url, ProfilePayload, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerPrfile --- "+ ststuscode);
		
		while (ststuscode == 504) {
			closeableHttpResponse =restclient.put(url, ProfilePayload, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerPrfile --- "+ ststuscode);
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		}else {
			System.out.println(jsonObj);
		}
		
	//	Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
	}
	
	@Test(priority=4,enabled =true)
	public void UpdateIncome() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/income";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		IncomeData ID = new IncomeData();
		String IncomePayload = ID.IncomePayload();
		
		closeableHttpResponse =restclient.put(url, IncomePayload, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerIncome --- "+ ststuscode);
		
		while (ststuscode == 504) {
			closeableHttpResponse =restclient.put(url, IncomePayload, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerincome --- "+ ststuscode);
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		}else {
			System.out.println(jsonObj);
		}
	}
	
	@Test(priority=5,enabled= true)
	public void updatedeclaration() throws ClientProtocolException, IOException {
		
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/declaration";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		DeclarationData DD = new DeclarationData();
		String DeclarationPayload = DD.DeclarationPayload();
		
		closeableHttpResponse =restclient.put(url, DeclarationPayload, headermap);
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerDeclaration --- "+ ststuscode);
		
		while (ststuscode == 504) {
			closeableHttpResponse =restclient.put(url, DeclarationPayload, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerDeclaration --- "+ ststuscode);
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		}else {
			System.out.println(jsonObj);
		}
	}
	
	@Test(priority=6,enabled=true)
	public void getBorrowerPrfile() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/profile";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerPrfile --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=7,enabled=true)
	public void getBorrowerIncome() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/income";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerIncome --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=8,enabled=true)
	public void getBorrowerAsset() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/asset";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerAsset --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=9,enabled=false)
	public void getBorrowerLiabilities() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/"+loanid+"/borrower/"+email+"/liabilities";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerLiabilities --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=10,enabled=false)
	public void getBorrowerDeclaration() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/declaration";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerDeclaration --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=8,enabled=false)
	public void getApplicantsByLoanId() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicantsByLoanId --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);
				
	}
	
	@Test(priority=11,enabled=false)
	public void getLoansByBorrowerUsername() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/"+ email+ "/loans";
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
	
	@Test(priority=12,enabled=false)
	public void getBorrowerAcountDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/bankaccounts";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerAcountDetails --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=13,enabled=false)
	public void getBorrowerByUsername() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/"+email;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerByUsername --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=14,enabled=false)
	public void getLoanLogs() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid+"/log";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanLogs --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONArray jsonObj = new JSONArray(responseString);
		System.out.println("Get LoanLogs endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=15,enabled=false)
	public void getAllLoanDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/all";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllLoanDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");

		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get AllLoanDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {
			JSONArray jarray = new JSONArray(responseString);
			System.out.println("Get AllLoanDetails endpoin response : "+ jarray);
		}
						
	}
	
	@Test(priority=16,enabled=false)
	public void getAllActiveLoanDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/active";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllActiveLoanDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllActiveLoanDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get AllActiveLoanDetails endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=17,enabled=false)
	public void getAllApplicationsDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllApplicationsDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllApplicationsDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get AllApplicationsDetails endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=18,enabled=false)
	public void getAllCLosedLoansDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/closed";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllCLosedLoansDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllCLosedLoansDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get AllCLosedLoansDetails endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=19,enabled=false)
	public void getLoandata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoandata --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get Loandata endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get Loandata endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=20,enabled=false)
	public void getApplicationdata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/application/"+loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicationdata --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get Applicationdata endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get Applicationdata endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=21,enabled=false)
	public void getSummaryOfLoan() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/summary";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getSummaryOfLoan --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get SummaryOfLoan endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get SummaryOfLoan endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=22,enabled=false)
	public void getLoaninReviewStatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications/reviewLoans/mine";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoaninReviewStatus --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoaninReviewStatus endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get LoaninReviewStatus endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=23,enabled=false)
	public void getAllLoaninReviewStatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications/reviewLoans/all";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllLoaninReviewStatus --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllLoaninReviewStatus endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get AllLoaninReviewStatus endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=24,enabled=false)
	public void getBorrowerLogs() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/"+email+"/log";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerLogs --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerLogs endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get BorrowerLogs endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=25,enabled=false)
	public void getBorrowerList() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrowers";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerList --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerList endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get BorrowerList endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=26,enabled=false)
	public void getBorrowerProfileDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/"+email;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerProfileDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerProfileDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get BorrowerProfileDetails endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=27,enabled=false)
	public void getLoanOfficerList() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficers";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanOfficerList --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoanOfficerList endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get LoanOfficerList endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=28,enabled=false)
	public void getLoanOfficerDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/"+users.getUsername();
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanOfficerDetails --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoanOfficerDetails endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get LoanOfficerDetails endpoin response : "+ jsonObj);
		}
		
		LOdetailsVerification lodetailsVerification = new LOdetailsVerification();
		lodetailsVerification.responsevalidation(jsonObj);
	}
	
	@Test(priority=29,enabled=false)
	public void getLoansOfLoanofficer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/"+users.getUsername()+"/loans";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoansOfLoanofficer --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoansOfLoanofficer endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get LoansOfLoanofficer endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=30,enabled=false)
	public void getBrandedCodeOfLoanofficer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/"+users.getUsername()+"/branded";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBrandedCodeOfLoanofficer --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BrandedCodeOfLoanofficer endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get BrandedCodeOfLoanofficer endpoin response : "+ jsonObj);
		}				
	}
	
	@Test(priority=31,enabled=false)
	public void RedirectToSummaryScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/"+loanid;
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
	
	@Test(priority=12,enabled=false)
	public void getApplicationData() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/application/"+loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicationData --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=13,enabled=false)
	public void getreadFullLoanContainer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/test/loan/"+loanid+"/readFullLoanContainer";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getreadFullLoanContainer --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=14,enabled=false)
	public void UpdateLoanstatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/UpdateStatus";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		String body = "{\r\n" + 
				"	\"status\": \"EnumLoanStatusProcessing\"\r\n" + 
				"} " ;
		
		closeableHttpResponse =restclient.put(url, body, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of UpdateLoanstatus --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : "+ jsonObj);				
	}
	
	@Test(priority=15,enabled=false)
	public void getAllConditionsDocuments() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/documents";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllConditionsDocuments --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONArray jsonArray = new JSONArray(responseString);
		JSONObject jObj = jsonArray.getJSONObject(0);
		System.out.println("getAllConditionsDocuments endpoin response : "+ jObj);	
		DocumetnID = jObj.getString("id");
		
	}
	
	@Test(priority=16,enabled=false)
	public void getPerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/document/"+DocumetnID;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getPerticularConditionsDocument --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("getPerticularConditionsDocument endpoin response : "+ jsonObj);	
		
	}
	
	@Test(priority=17,enabled=false)
	public void DownloadPerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/document/"+DocumetnID;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of DownloadPerticularConditionsDocument --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("DownloadPerticularConditionsDocument endpoin response : "+ jsonObj);	
		
	}
	
	@Test(priority=18,enabled=false)
	public void DeletePerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/"+DocumetnID+"/DeleteDocument";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.delete(url, headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of DownloadPerticularConditionsDocument --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("DownloadPerticularConditionsDocument endpoin response : "+ jsonObj);	
		
	}
	
	@Test(priority=19,enabled=false)
	public void getAllCondition() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/conditions";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		closeableHttpResponse =restclient.get(url,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllCondition --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONArray jsonArray = new JSONArray(responseString);
		JSONObject jObj = jsonArray.getJSONObject(1);
		System.out.println("getAllConditionsDocuments endpoin response : "+ jObj);	
		DocumetnID = jObj.getString("id");
		
	}
	
	@Test(priority=20,enabled = false)
	public void SubmitCondition() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/"+closedloanid+"/conditions";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "multipart/form-data");
		headermap.put("Authorization", token);
		
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("explanation", "app_user"));
        
		closeableHttpResponse =restclient.postwihformdata(url,urlParameters,headermap);
		
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of SubmitCondition --- "+ ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("SubmitCondition endpoin response : "+ jsonObj);
		
	}

	@Test(priority=32,enabled=false)
	public void RedirectToDocumentScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/"+loanid+"/documents";
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
		System.out.println("Status code of RedirectToDocumentScreen --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToDocumentScreen endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get RedirectToDocumentScreen endpoin response : "+ responseString);
		}								
	}

	@Test(priority=33,enabled=false)
	public void RedirectToConditionsScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/"+loanid+"/conditions";
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
		System.out.println("Status code of RedirectToConditionsScreen --- "+ ststuscode);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		
		if(ststuscode!=STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToConditionsScreen endpoin response : "+ jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		}else {	
			System.out.println("Get RedirectToConditionsScreen endpoin response : "+ responseString);
		}								
	}
	
	
}
