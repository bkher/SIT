package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.client.restClient;
import com.qa.data.CreateLoansUsingInvitationToken;
import com.qa.data.InviteUserData;
import com.qa.data.users;
import com.qa.utils.testUtils;

import restAPI.TestBase;

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
	
	@Test(priority=1, enabled= true)
	public void inviteBorrower() throws JsonGenerationException, JsonMappingException, IOException {
		
		String url = Envurl + "/api/v1/borrower/invite" ;
		
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		ObjectMapper mapper =  new ObjectMapper();
		email =testUtils.generateRandomEmail();
		System.out.println("email id "+email);
		InviteUserData inviteClass =  new InviteUserData("Bhagat","Kher",email,"9898016454");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), inviteClass);
		String UserJsonString = mapper.writeValueAsString(inviteClass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		invitationUrl = utilitobj.getvlueByJpath(jsonObj,"invitationUrl");
		invitationToken = utilitobj.getvlueByJpath(jsonObj,"invitationToken");
		
	}
	
	
	@Test(priority=2, enabled= true)
	public void CreateBorrower() throws JsonGenerationException, JsonMappingException, IOException {
		
		String url = Envurl + "/api/v1/loans/create" ;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		ObjectMapper mapper =  new ObjectMapper();
		CreateLoansUsingInvitationToken createuserclass =  new CreateLoansUsingInvitationToken("EnumTransactionTypePurchase",invitationToken,email,"Docabc@123");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), createuserclass);
		String UserJsonString = mapper.writeValueAsString(createuserclass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		loanid = utilitobj.getvlueByJpath(jsonObj,"loanId");
		System.out.println("Loan id " + loanid);
		
	}
	
	
	@Test(priority=3)
	public void UpdateBorrowerProfile() throws ClientProtocolException, IOException {
		
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/profileJob" ;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		String body = "{\r\n" + 
				"    \"firstName\": \"Bhagat\",\r\n" + 
				"    \"middleName\": \"\",\r\n" + 
				"    \"lastName\": \"Kher\",\r\n" + 
				"    \"suffix\": \"Mr.\",\r\n" + 
				"    \"borrowerBirthDate\": \"1989-09-23T00:00:00\",\r\n" + 
				"    \"ssn\": \"897258934\",\r\n" + 
				"    \"preferredEmail\": \"" +email+ "\",\r\n" + 
				"    \"phone\": \"9876543290\",\r\n" + 
				"    \"cellPhone\": \"9876543290\",\r\n" + 
				"    \"communication\": [\r\n" + 
				"        \"EnumCommunicationText\",\r\n" + 
				"        \"EnumCommunicationEmail\"\r\n" + 
				"    ],\r\n" + 
				"    \"selectedProperty\": \"EnumSelectedPropertyTrue\",\r\n" + 
				"    \"inContract\": \"EnumInContractTrue\",\r\n" + 
				"    \"propertyType\": \"EnumBorrowerPropertyTypeMultiFamily\",\r\n" + 
				"    \"propertyTypeUnits\": \"EnumBorrowerPropertyTypeUnits3\",\r\n" + 
				"    \"propertyUse\": \"EnumPropertyUseSecondHome\",\r\n" + 
				"    \"propertyAddress\": {\r\n" + 
				"        \"country\": null,\r\n" + 
				"        \"line1\": \"TBD\",\r\n" + 
				"        \"line2\": null,\r\n" + 
				"        \"city\": null,\r\n" + 
				"        \"state\": null,\r\n" + 
				"        \"zipCode\": null\r\n" + 
				"    },\r\n" + 
				"    \"expectedMonthlyRentalIncome\": 700.0,\r\n" + 
				"    \"city\": \"qw\",\r\n" + 
				"    \"zipCode\": \"13231\",\r\n" + 
				"    \"county\": null,\r\n" + 
				"    \"state\": \"AL\",\r\n" + 
				"    \"loanAmount\": {\r\n" + 
				"        \"purchasePrice\": 78000.0,\r\n" + 
				"        \"downPayment\": 9360.0,\r\n" + 
				"        \"loanPercentage\": 0.0,\r\n" + 
				"        \"loanAmount\": 68640.0\r\n" + 
				"    },\r\n" + 
				"    \"refinanceCashOutIndicator\": \"EnumBorrowerRefinanceCashOutIndicatorBlank\",\r\n" + 
				"    \"cashOutAmount\": null,\r\n" + 
				"    \"cashOutType\": null,\r\n" + 
				"    \"individualOrJointCredit\": \"EnumBorrowerIndividualOrJointCreditJoint\",\r\n" + 
				"    \"maritalStatus\": \"EnumBorrowerMaritalStatusMarried\",\r\n" + 
				"    \"addSpouseAsCoBorrower\": \"EnumBorrowerAddSpouseAsCoBorrowerTrue\",\r\n" + 
				"    \"spouseFirstName\": \"Spouse\",\r\n" + 
				"    \"spouseMiddleName\": \"\",\r\n" + 
				"    \"spouseLastName\": \"Kher\",\r\n" + 
				"    \"spouseSuffix\": \"\",\r\n" + 
				"    \"spouseBirthDate\": \"2000-09-20T00:00:00\",\r\n" + 
				"    \"spouseSsn\": \"087236712\",\r\n" + 
				"    \"spousePreferredEmail\": \"bhagatsinh.kher+21082020_1.4sp@lendfoundry.com\",\r\n" + 
				"    \"spouseCellPhone\": \"8723517253\",\r\n" + 
				"    \"addOtherCoBorrowers\": \"EnumBorrowerAddAsNewCoBorrowerFalse\",\r\n" + 
				"    \"vaEligible\": \"EnumBorrowerVAEligibleTrue\",\r\n" + 
				"    \"vaSelectOptions\": \"EnumVASurvivingSpouse\",\r\n" + 
				"    \"vaCurrentlyServingExpirationDateValue\": null,\r\n" + 
				"    \"existingVaLoan\": \"EnumBorrowerExistingVALoanTrue\",\r\n" + 
				"    \"monthlyChildCareVa\": \"EnumBorrowerMonthlyChildCareVATrue\",\r\n" + 
				"    \"monthlyChildCareMoneyVa\": 234,\r\n" + 
				"    \"realEstateAgent\": \"EnumBorrowerRealEstateAgentTrue\",\r\n" + 
				"    \"realEstateReferral\": \"EnumBorrowerNeedRealEstateReferralBlank\",\r\n" + 
				"    \"realEstateFirstName\": \"addd\",\r\n" + 
				"    \"realEstateLastName\": \"One\",\r\n" + 
				"    \"realEstateCompanyName\": \"rwo\",\r\n" + 
				"    \"realEstateEmail\": \"bhagatsinh.kher+0110_1.1ead@lendfoundry.com\",\r\n" + 
				"    \"realEstatePhoneNumber\": \"2452352352\",\r\n" + 
				"    \"haveLoanOfficer\": null,\r\n" + 
				"    \"searchLoanOfficer\": null,\r\n" + 
				"    \"spouseOccupied\": \"EnumBorrowerSpouseOccupiedFalse\",\r\n" + 
				"    \"rentOrOwn\": \"EnumBorrowerRentOrOwnRent\",\r\n" + 
				"    \"currentAddressSame\": \"EnumBorrowerCurrentAddressSameFalse\",\r\n" + 
				"    \"currentStreetAddress\": {\r\n" + 
				"         \"country\": \"us\",\r\n" + 
				"        \"line1\": \"Qwinn Court\",\r\n" + 
				"        \"line2\": \"\",\r\n" + 
				"        \"city\": \"Bay City\",\r\n" + 
				"        \"state\": \"MI\",\r\n" + 
				"        \"zipCode\": \"48706\"\r\n" + 
				"    },\r\n" + 
				"    \"currentAddressStartDate\": \"2010-08-01T00:00:00\",\r\n" + 
				"    \"differentMailingAddress\": \"EnumBorrowerDifferentMailingAddressFalse\",\r\n" + 
				"    \"mailingAddress\": {\r\n" + 
				"    \"line1\": \"string\",\r\n" + 
				"    \"line2\": \"string\",\r\n" + 
				"    \"city\": \"string\",\r\n" + 
				"    \"state\": \"string\",\r\n" + 
				"    \"zipCode\": \"string\"\r\n" + 
				"  },\r\n" + 
				"    \"additionalAddresses\":  [\r\n" + 
				"  ],\r\n" + 
				"    \"firstMortgagePIValue\": null,\r\n" + 
				"    \"otherFinancingPIValue\": null,\r\n" + 
				"    \"hazardInsuranceValue\": null,\r\n" + 
				"    \"realEstateTaxesValue\": null,\r\n" + 
				"    \"mortgageInsuranceValue\": null,\r\n" + 
				"    \"homeOwnerAssocDuesValue\": null,\r\n" + 
				"    \"combineMonthlyExpenseOtherValue\": null,\r\n" + 
				"    \"lookingToSell\": \"EnumBorrowerLookingToSellBlank\",\r\n" + 
				"    \"monthlyRentalAmount\": 2000.0,\r\n" + 
				"    \"reoInfo\": [\r\n" + 
				"        {\r\n" + 
				"            \"propertyAddress\": {\r\n" + 
				"                 \"country\": \"string\",\r\n" + 
				"                \"line1\": \"Stone Street\",\r\n" + 
				"                \"line2\": \"\",\r\n" + 
				"                \"city\": \"New York\",\r\n" + 
				"                \"state\": \"NY\",\r\n" + 
				"                \"zipCode\": \"10004\"\r\n" + 
				"            },\r\n" + 
				"            \"propertyType\": \"EnumBorrowerREOPropertyTypePlannedUnitDevelopment\",\r\n" + 
				"            \"propertyTypeUnits\": null,\r\n" + 
				"            \"propertyStatus\": \"EnumBorrowerREOPropertyStatusSold\",\r\n" + 
				"            \"propertyUse\": \"EnumBorrowerREOPropertyUseSecondHome\",\r\n" + 
				"            \"propertyValue\": 12312.0,\r\n" + 
				"            \"rentalIncome\": null,\r\n" + 
				"            \"insTaxDues\": 12312.0\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"oweFirst\": null,\r\n" + 
				"    \"oweSecond\": null,\r\n" + 
				"    \"oweOther\": null,\r\n" + 
				"    \"dependentsCount\": 1,\r\n" + 
				"    \"dependentsAges\": \"23\",\r\n" + 
				"    \"yearBuilt\": 2005,\r\n" + 
				"    \"originalPurchasePrice\": null\r\n" + 
				"}";
		
		closeableHttpResponse = restclient.post(url, body, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		ProfilejobId = utilitobj.getvlueByJpath(jsonObj,"jobId");
		System.out.println("profile update job id " + ProfilejobId);
		
		
	}
	
	
	@Test(priority=4)
	public void UpdateBorrowerIncome() throws ClientProtocolException, IOException {
		
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/incomeJob" ;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		
		String body = "{\r\n" + 
				"    \"employers\": [\r\n" + 
				"        {\r\n" + 
				"            \"currentlyWork\": \"EnumBorrowerCurrentlyWorkTrue\",\r\n" + 
				"            \"name\": \"123\",\r\n" + 
				"            \"title\": \"13\",\r\n" + 
				"            \"phone\": \"2131312312\",\r\n" + 
				"            \"startDate\": \"1999-12-01T00:00:00\",\r\n" + 
				"            \"endDateIsPresent\": false,\r\n" + 
				"            \"endDate\": null,\r\n" + 
				"            \"workYear\": 12,\r\n" + 
				"            \"workMonths\": 11,\r\n" + 
				"            \"baseSalary\": 13,\r\n" + 
				"            \"monthlyBonus\": 123,\r\n" + 
				"            \"commission\": 123,\r\n" + 
				"            \"overtime\": 333,\r\n" + 
				"            \"address\": {\r\n" + 
				"            	\"country\": \"us\",\r\n" + 
				"                \"line1\": \" Asdee Lane\",\r\n" + 
				"                \"line2\": \"3\",\r\n" + 
				"                \"city\": \"Woodbridge\",\r\n" + 
				"                \"state\": \"VA\",\r\n" + 
				"                \"zipCode\": \"22192\"\r\n" + 
				"            },\r\n" + 
				"            \"otherOptionalIncome\": [\r\n" + 
				"                \"EnumBorrowerOtherOptionalIncomeBonus\",\r\n" + 
				"                \"EnumBorrowerOtherOptionalIncomeCommission\",\r\n" + 
				"                \"EnumBorrowerOtherOptionalIncomeOvertime\"\r\n" + 
				"            ],\r\n" + 
				"            \"commissionIncome\": \"EnumBorrowerEmployerCommissionIncomeTrue\",\r\n" + 
				"            \"borrowerEmployed\": null\r\n" + 
				"        }\r\n" + 
				"     \r\n" + 
				"    ],\r\n" + 
				"    \"business\": [\r\n" + 
				"        {\r\n" + 
				"            \"currentlyWork\": \"EnumBorrowerCurrentlyWorkTrue\",\r\n" + 
				"            \"monthtlyIncomeAmount\": 123,\r\n" + 
				"            \"companyName\": \"sadas\",\r\n" + 
				"            \"designation\": \"123123\",\r\n" + 
				"            \"companyAddress\": {\r\n" + 
				"            	\"country\": \"us\",\r\n" + 
				"            	\"line1\": \" Asdee Lane\",\r\n" + 
				"                \"line2\": \"3\",\r\n" + 
				"                \"city\": \"Woodbridge\",\r\n" + 
				"                \"state\": \"VA\",\r\n" + 
				"                \"zipCode\": \"22192\"\r\n" + 
				"            },\r\n" + 
				"            \"phone\": \"1231231231\",\r\n" + 
				"            \"startDate\": \"1999-11-01T00:00:00\",\r\n" + 
				"            \"endDate\": null,\r\n" + 
				"            \"endDateIsPresent\": true,\r\n" + 
				"            \"own25Percent\": \"EnumBorrowerBusinessOwn25percentTrue\",\r\n" + 
				"            \"businessType\": \"EnumBorrowerBusinessTypePartnership\"\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"militaryPay\": 3123123\r\n" + 
				"}";
		
		closeableHttpResponse = restclient.post(url, body, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		
		incomejobId = utilitobj.getvlueByJpath(jsonObj,"jobId");
		System.out.println("income update job id " + incomejobId);
			
	}
	
	@Test(priority=5)
	public void UpdateBorrowerDeclaration() throws ClientProtocolException, IOException {
		
		String url = Envurl + "/api/v1/loans/"+loanid+"/borrower/"+email+"/declarationjob" ;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
	
		String body ="{\r\n" + 
				"    \"outstandingJudgement\": \"EnumBorrowerOutstandingJudgementTrue\",\r\n" + 
				"    \"outstandingJudgementExplanation\": \"t1\",\r\n" + 
				"    \"declaredBankrupt\": \"EnumBorrowerDeclaredBankruptFalse\",\r\n" + 
				"    \"declaredBankruptExplanation\": \"t2\",\r\n" + 
				"    \"propertyForeclosed\": \"EnumBorrowerPropertyForeclosedFalse\",\r\n" + 
				"    \"propertyForeclosedExplanation\": \"t3\",\r\n" + 
				"    \"partyLawsuit\": \"EnumBorrowerPartyLawsuitFalse\",\r\n" + 
				"    \"partyLawsuitExplanation\": \"t4\",\r\n" + 
				"    \"beenObligated\": \"EnumBorrowerBeenObligatedFalse\",\r\n" + 
				"    \"beenObligatedExplanation\": \"t5\",\r\n" + 
				"    \"presentlyDelinquent\": \"EnumBorrowerPresentlyDelinquentFalse\",\r\n" + 
				"    \"presentlyDelinquentExplanation\": \"t6\",\r\n" + 
				"    \"obligatedToPayAlimony\": \"EnumBorrowerObligatedToPayAlimonyFalse\",\r\n" + 
				"    \"obligatedToPayAlimonyExplanation\": \"t7\",\r\n" + 
				"    \"downPaymentBorrowed\": \"EnumBorrowerDownPaymentBorrowedFalse\",\r\n" + 
				"    \"downPaymentBorrowedExplanation\": \"t8\",\r\n" + 
				"    \"endoserOnNote\": \"EnumBorrowerEndoserOnNoteFalse\",\r\n" + 
				"    \"endoserOnNoteExplanation\": \"t9\",\r\n" + 
				"    \"usCitizen\": \"EnumBorrowerUSCitizenFalse\",\r\n" + 
				"    \"permanentResidentAlien\": \"EnumBorrowerPermanentResidentAlienFalse\",\r\n" + 
				"    \"intentToOccupy\": \"EnumBorrowerIntentToOccupyFalse\",\r\n" + 
				"    \"ownshipInterest\": \"EnumBorrowerOwnshipInterestFalse\",\r\n" + 
				"    \"typeOfPropertyOwn\": \"EnumBorrowerTypeOfPropertyOwnIP\",\r\n" + 
				"    \"holdTitle\": \"EnumBorrowerHoldTitleJAP\",\r\n" + 
				"    \"ethnicityDoNotWish\": \"EnumBorrowerEthnicityDoNotWishTrue\",\r\n" + 
				"    \"HispanicOrLatino\": [\r\n" + 
				"        \"EnumBorrowerHispanicOrLatinoHL\",\r\n" + 
				"        \"EnumBorrowerHispanicOrLatinoTypeCU\",\r\n" + 
				"        \"EnumBorrowerHispanicOrLatinoTypeMX\",\r\n" + 
				"        \"EnumBorrowerHispanicOrLatinoTypeOHL\",\r\n" + 
				"        \"EnumBorrowerHispanicOrLatinoTypePR\"\r\n" + 
				"    ],\r\n" + 
				"    \"hispanicOrLatinoTypeOHL\": \"sss\",\r\n" + 
				"    \"hispanicOrLatinoNHL\": null,\r\n" + 
				"    \"raceDoNotWish\": \"EnumBorrowerRaceDoNotWishFalse\",\r\n" + 
				"    \"residancy\": \"EnumBorrowerResidancyAI\",\r\n" + 
				"    \"enrolledPrincipalTribe\": \"\",\r\n" + 
				"    \"AsianType\": [\r\n" + 
				"        \"EnumBorrowerAsianTypeAI\",\r\n" + 
				"        \"EnumBorrowerAsianTypeAS\",\r\n" + 
				"        \"EnumBorrowerAsianTypeCH\",\r\n" + 
				"        \"EnumBorrowerAsianTypeJP\",\r\n" + 
				"        \"EnumBorrowerAsianTypeKR\",\r\n" + 
				"        \"EnumBorrowerAsianTypeOA\",\r\n" + 
				"        \"EnumBorrowerAsianTypeVN\",\r\n" + 
				"        \"EnumBorrowerAsianType\"\r\n" + 
				"    ],\r\n" + 
				"    \"enterOtherAsianRace\": \"q\",\r\n" + 
				"    \"blackOrAfrician\": \"EnumBorrowerResidancyBAA\",\r\n" + 
				"    \"ResidancyNH\": [\r\n" + 
				"        \"EnumBorrowerNativeHawaiianOrOtherGC\",\r\n" + 
				"        \"EnumBorrowerNativeHawaiianOrOtherNH\",\r\n" + 
				"        \"EnumBorrowerEnterOtherPacificIslanderRace\",\r\n" + 
				"        \"EnumBorrowerResidancyNH\"\r\n" + 
				"    ],\r\n" + 
				"    \"residancyWH\": \"EnumBorrowerResidancyWH\",\r\n" + 
				"    \"enterOtherPacificIslanderRace\": \"1\",\r\n" + 
				"    \"sexDoNotWish\": \"EnumBorrowerSexDoNotWishTrue\",\r\n" + 
				"     \"familyRelationshipwithSeller\": \"EnumDeclarationFamilyRelationshipwithSellerTrue\",\r\n" + 
				"  \"otherMortgageLoanonOtherProperty\": \"EnumDeclarationOtherMortgageLoanonOtherPropertyTrue\",\r\n" + 
				"  \"newCreditLoanonBeforeClosing\": \"EnumDeclarationNewCreditLoanonBeforeClosingTrue\",\r\n" + 
				"  \"ifLienSubject\": \"EnumDeclarationIfLienSubjectTrue\",\r\n" + 
				"  \"propertyForeclosedConveyedTitle\": \"EnumBorrowerPropertyForeclosedConveyedTitleTrue\",\r\n" + 
				"  \"preForeClosureShortSale\": \"EnumDeclarationPreForeClosureShortSaleTrue\",\r\n" + 
				"  \"declaredBankruptChapter\": [\r\n" + 
				"    \"EnumBorrowerDeclaredBankruptChapter7\"\r\n" + 
				"  ],\r\n" + 
				"  \"citizenship\": \"EnumBorrowerUSCitizen\",\r\n" + 
				"  \"ethnicityCollected\": \"EnumBorrowerEthnicityCollectedTrue\",\r\n" + 
				"  \"raceCollected\": \"EnumBorrowerRaceCollectedTrue\",\r\n" + 
				"  \"sexCollected\": \"EnumBorrowerSexCollectedTrue\",\r\n" + 
				"  \"familyRelationshipwithSellerExplanation\": \"12312\",\r\n" + 
				"  \"downPaymentBorrowedAmount\": \"23121\",\r\n" + 
				"  \"otherMortgageLoanonOtherPropertyExplanation\": \"string\",\r\n" + 
				"  \"newCreditLoanonBeforeClosingExplanation\": \"string\",\r\n" + 
				"  \"ifLienSubjectExplanation\": \"string\",\r\n" + 
				"  \"propertyForeclosedConveyedTitleExplanation\": \"string\",\r\n" + 
				"  \"preForeClosureShortSaleExplanation\": \"string\"\r\n" + 
				"    \r\n" + 
				"}";
		
		closeableHttpResponse = restclient.post(url, body, headermap);
		String responseString =EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		declarationjobId = utilitobj.getvlueByJpath(jsonObj,"jobId");
		System.out.println("declaration update job id " + declarationjobId);
		
	}	
	
}
