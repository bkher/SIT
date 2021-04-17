package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
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

import TestData.ProfileData;
import restAPI.TestBase;
import validations.CreateBorrwerResponseValdation;
import validations.InviteBorrwerResponseValdation;
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
		String url = Envurl + "/api/v1/jwt/auth";

		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");

		ObjectMapper mapper = new ObjectMapper();
		users = new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);

		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		token = utilitobj.getvlueByJpath(jsonObj, "token");
		token = "Bearer " + token;
	}

	@Test(priority = 1, enabled = true)
	public void inviteBorrower() throws JsonGenerationException, JsonMappingException, IOException {

		String url = Envurl + "/api/v1/borrower/invite";

		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		ObjectMapper mapper = new ObjectMapper();
		email = testUtils.generateRandomEmail();
		InviteUserData inviteClass = new InviteUserData("Bhagat", "Kher", email, "9898016454");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), inviteClass);
		String UserJsonString = mapper.writeValueAsString(inviteClass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		} else {
			System.out.println(jsonObj);
		}

		invitationUrl = utilitobj.getvlueByJpath(jsonObj, "invitationUrl");
		invitationToken = utilitobj.getvlueByJpath(jsonObj, "invitationToken");

		InviteBorrwerResponseValdation inviteValidation = new InviteBorrwerResponseValdation();
		inviteValidation.responsevalidation(jsonObj);

	}

	@Test(priority = 2, enabled = true)
	public void CreateBorrower() throws JsonGenerationException, JsonMappingException, IOException {

		String url = Envurl + "/api/v2/loans/create";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);
		ObjectMapper mapper = new ObjectMapper();
		CreateLoansUsingInvitationToken createuserclass = new CreateLoansUsingInvitationToken(
				"EnumTransactionTypePurchase", invitationToken, email, "Docabc@123");
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"),
				createuserclass);
		String UserJsonString = mapper.writeValueAsString(createuserclass);
		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of CreateBorrower --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		} else {
			System.out.println(jsonObj);
		}

		loanid = utilitobj.getvlueByJpath(jsonObj, "loanId");
		System.out.println("Loan id " + loanid);

		CreateBorrwerResponseValdation CreateBorrowerValidation = new CreateBorrwerResponseValdation();
		CreateBorrowerValidation.responsevalidation(jsonObj);

	}

	@Test(priority = 3, enabled = true)
	public void updateprofiledata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/profile";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		String profileUpdateData = "{\r\n" + "  \"firstName\": \"Bhagat\",\r\n" + "  \"middleName\": \"G\",\r\n"
				+ "  \"lastName\": \"Kher\",\r\n" + "  \"suffix\": \"Mr.\",\r\n"
				+ "  \"borrowerBirthDate\": \"2000-03-20T06:40:58.361Z\",\r\n" + "  \"ssn\": \"888123987\",\r\n"
				+ "  \"preferredEmail\": \"" + email + "\",\r\n" + "  \"phone\": \"9898006789\",\r\n"
				+ "  \"cellPhone\": \"9979988999\",\r\n" + "  \"communication\": [\r\n"
				+ "    \"EnumCommunicationEmail\",\r\n" + "    \"EnumCommunicationPhone\"\r\n" + "  ],\r\n"
				+ "  \"selectedProperty\": \"EnumSelectedPropertyTrue\",\r\n"
				+ "  \"inContract\": \"EnumInContractTrue\",\r\n"
				+ "  \"propertyType\": \"EnumBorrowerPropertyTypeMultiFamily\",\r\n"
				+ "  \"propertyTypeUnits\": \"EnumBorrowerPropertyTypeUnits1\",\r\n"
				+ "  \"propertyUse\": \"EnumPropertyUseFHASecondaryResidence\",\r\n" + "  \"propertyAddress\": {\r\n"
				+ "    \"country\": \"US\",\r\n" + "    \"line1\": \"4502 Medical Drive\",\r\n"
				+ "    \"line2\": null,\r\n" + "    \"city\": \"San Antonio\",\r\n" + "    \"state\": \"TX\",\r\n"
				+ "     \"zipCode\": \"78229\"\r\n" + "  },\r\n" + "  \"expectedMonthlyRentalIncome\": 0,\r\n"
				+ "  \"city\": \"San Antonio\",\r\n" + "  \"zipCode\": \"78229\",\r\n" + "  \"county\": \"US\",\r\n"
				+ "  \"state\": \"TX\",\r\n" + "  \"loanAmount\": {\r\n" + "    \"purchasePrice\": 400000,\r\n"
				+ "    \"downPayment\": 92000,\r\n" + "    \"loanPercentage\": 23,\r\n"
				+ "    \"loanAmount\": 308000\r\n" + "  },\r\n"
				+ "  \"refinanceCashOutIndicator\": \"EnumBorrowerRefinanceCashOutIndicatorBlank\",\r\n"
				+ "  \"cashOutAmount\": 0,\r\n" + "  \"cashOutType\": \"EnumBorrowerCashOutTypeDebtConsolidation\",\r\n"
				+ "  \"individualOrJointCredit\": \"EnumBorrowerIndividualOrJointCreditIndividual\",\r\n"
				+ "  \"maritalStatus\": \"EnumBorrowerMaritalStatusMarried\",\r\n"
				+ "  \"addSpouseAsCoBorrower\": \"EnumBorrowerAddSpouseAsCoBorrowerBlank\",\r\n"
				+ "  \"spouseFirstName\": null,\r\n" + "  \"spouseMiddleName\": null,\r\n"
				+ "  \"spouseLastName\": null,\r\n" + "  \"spouseSuffix\": null,\r\n"
				+ "  \"spouseBirthDate\": null,\r\n" + "  \"spouseSsn\": null,\r\n"
				+ "  \"spousePreferredEmail\": null,\r\n" + "  \"spouseCellPhone\": null,\r\n"
				+ "  \"addOtherCoBorrowers\": null,\r\n" + "  \"vaEligible\": \"EnumBorrowerVAEligibleTrue\",\r\n"
				+ "  \"vaSelectOptions\": \"BorrowerVACurrentlyServingExpirationDateValue\",\r\n"
				+ "  \"vaCurrentlyServingExpirationDateValue\": \"2025-03-20T06:40:58.361Z\",\r\n"
				+ "  \"existingVaLoan\": \"EnumBorrowerExistingVALoanTrue\",\r\n"
				+ "  \"monthlyChildCareVa\": \"EnumBorrowerMonthlyChildCareVATrue\",\r\n"
				+ "  \"monthlyChildCareMoneyVa\": 3000,\r\n"
				+ "  \"realEstateAgent\": \"EnumBorrowerRealEstateAgentFalse\",\r\n"
				+ "  \"realEstateReferral\": \"EnumBorrowerNeedRealEstateReferralTrue\",\r\n"
				+ "  \"realEstateFirstName\": null,\r\n" + "  \"realEstateLastName\": null,\r\n"
				+ "  \"realEstateCompanyName\": null,\r\n" + "  \"realEstateEmail\": null,\r\n"
				+ "  \"realEstatePhoneNumber\": null,\r\n" + "  \"haveLoanOfficer\": null,\r\n"
				+ "  \"searchLoanOfficer\": null,\r\n"
				+ "  \"spouseOccupied\": \"EnumBorrowerSpouseOccupiedBlank\",\r\n"
				+ "  \"rentOrOwn\": \"EnumBorrowerRentOrOwnRent\",\r\n"
				+ "  \"currentAddressSame\": \"EnumBorrowerCurrentAddressSameFalse\",\r\n"
				+ "  \"currentStreetAddress\": {\r\n" + "    \"country\": \"US\",\r\n"
				+ "    \"line1\": \"440 West 114th Street\",\r\n" + "    \"line2\": \"string\",\r\n"
				+ "    \"city\": \"New York\",\r\n" + "    \"state\": \"New York\",\r\n"
				+ "    \"zipCode\": \"10025\"\r\n" + "  },\r\n"
				+ "  \"currentAddressStartDate\": \"2000-03-20T06:40:58.361Z\",\r\n"
				+ "  \"differentMailingAddress\": \"EnumBorrowerDifferentMailingAddressFalse\",\r\n"
				+ "  \"mailingAddress\": null,\r\n" + "  \"additionalAddresses\": null,\r\n"
				+ "  \"firstMortgagePIValue\": null,\r\n" + "  \"otherFinancingPIValue\": null,\r\n"
				+ "  \"hazardInsuranceValue\": null,\r\n" + "  \"realEstateTaxesValue\": null,\r\n"
				+ "  \"mortgageInsuranceValue\": null,\r\n" + "  \"homeOwnerAssocDuesValue\": null,\r\n"
				+ "  \"combineMonthlyExpenseOtherValue\": null,\r\n"
				+ "  \"supplementalPropertyInsuranceValue\": null,\r\n"
				+ "  \"lookingToSell\": \"EnumBorrowerLookingToSellBlank\",\r\n"
				+ "  \"monthlyRentalAmount\": 2000,\r\n" + "  \"reoInfo\": [\r\n" + "    {\r\n"
				+ "      \"propertyAddress\": {\r\n" + "        \"country\": \"US\",\r\n"
				+ "        \"line1\": \"330 Brookline Avenue\",\r\n" + "        \"line2\": \"string\",\r\n"
				+ "        \"city\": \"Boston\",\r\n" + "        \"state\": \"Massachusetts\",\r\n"
				+ "        \"zipCode\": \"02215\"\r\n" + "      },\r\n"
				+ "      \"propertyType\": \"EnumBorrowerREOPropertyTypeCondominium\",\r\n"
				+ "      \"propertyTypeUnits\": null,\r\n"
				+ "      \"propertyStatus\": \"EnumBorrowerREOPropertyStatusPendingSale\",\r\n"
				+ "      \"propertyUse\": \"EnumBorrowerREOPropertyUseFHASecondaryResidence\",\r\n"
				+ "      \"propertyValue\": 20000,\r\n" + "      \"rentalIncome\": 10000,\r\n"
				+ "      \"insTaxDues\": 5000\r\n" + "    }\r\n" + "  ],\r\n" + "  \"oweFirst\": 0,\r\n"
				+ "  \"oweSecond\": 0,\r\n" + "  \"oweOther\": 0,\r\n"
				+ "  \"dependentsNumber\": \"EnumBorrowerDependentsOne\",\r\n" + "  \"dependentsAges\": 21,\r\n"
				+ "  \"yearBuilt\": 2020,\r\n" + "  \"originalPurchasePrice\": 10000,\r\n"
				+ "  \"uaLegalPropertyRightonProperty\": null,\r\n" + "  \"uaTypeofRightonPropertyYes\": null,\r\n"
				+ "  \"uaOtherExplanation\": null,\r\n" + "  \"uaStateofFormationofRelationship\": null,\r\n"
				+ "  \"borrowerCountry\": null,\r\n" + "  \"yesManufacturedHome\": \"EnumYesManufacturedHomeTrue\",\r\n"
				+ "  \"yesMixedUseProperty\": \"EnumYesMixedUsePropertyFalse\",\r\n"
				+ "  \"borrowerAlternateNames\": \"BGT\",\r\n"
				+ "  \"borrowerConsent\": \"EnumBorrowerConsentFalse\",\r\n"
				+ "  \"borrowerFirstHomeBuyer\": \"EnumBorrowerFirstHomeBuyerTrue\",\r\n"
				+ "  \"borrowerNonPermanentResidentAlien\": null,\r\n"
				+ "  \"loanAmortizationMaximumTermMonthsCount\": \"EnumLoanAmortizationMaximumTermMonthsCount120\",\r\n"
				+ "  \"loanMaturityPeriodCount\": \"EnumLoanMaturityPeriodCount120\",\r\n"
				+ "  \"mortgageType\": \"EnumMortgageTypeConventional\",\r\n"
				+ "  \"amortizationType\": \"EnumAmortizationTypeARM\",\r\n"
				+ "  \"communityPropertyState\": \"EnumBorrowerCommunityPropertyStateAtLeastOne\",\r\n"
				+ "  \"transactionDetails\": \"EnumBorrowerTransactionDetailsConstructionConversion\",\r\n"
				+ "  \"energyImprovement\": \"EnumBorrowerEnergyImprovementEnergyRelatedImprovement\",\r\n"
				+ "  \"titleType\": \"EnumBorrowerTitleTypeJointTenancy\",\r\n"
				+ "  \"trustInformation\": \"EnumBorrowerTrustInformationLandTrust\",\r\n"
				+ "  \"indianCountryLandTenure\": \"EnumBorrowerIndianCountryLandTenureAlaskaNative\",\r\n"
				+ "  \"mortgageLienType\": \"EnumMortgageLienTypeFirstLien\",\r\n"
				+ "  \"adjustableRate\": \"EnumAdjustableRateFalse\",\r\n"
				+ "  \"firstAdjustableRateOtherMonths\": \"EnumFirstAdjustableRateOtherMonths12\",\r\n"
				+ "  \"loanFeatures\": \"EnumBorrowerLoanFeaturesFalse\",\r\n"
				+ "  \"projectType\": \"EnumBorrowerProjectTypeCondominium\",\r\n"
				+ "  \"refinanceType\": \"EnumRefinanceTypeCashOut\",\r\n"
				+ "  \"refinanceProgram\": \"EnumRefinanceProgramFullDocumentation\",\r\n"
				+ "  \"trasactionCC\": \"EnumBorrowerTrasactionCCSingleClosing\",\r\n"
				+ "  \"subsequentAdjustableRateOtherMonths\": \"EnumSubsequentAdjustableRateOtherMonths12\",\r\n"
				+ "  \"adjustableRateSubsequentAdjustment\": \"EnumAdjustableRateSubsequentAdjustmentTrue\",\r\n"
				+ "  \"loanFeatureBalloonTerm\": \"EnumBorrowerLoanFeatureBalloonTerm12\",\r\n"
				+ "  \"loanFeaturesInterestOnlyTerm\": \"EnumBorrowerLoanFeaturesInterestOnlyTermTrue\",\r\n"
				+ "  \"loanFeaturesInterestOnlyTermMonths\": \"EnumBorrowerLoanFeaturesInterestOnlyTerm12\",\r\n"
				+ "  \"loanFeaturesNegativeAmortization\": \"EnumBorrowerLoanFeaturesNegativeAmortizationTrue\",\r\n"
				+ "  \"loanFeaturesPrePayPenaltyTerm\": \"EnumBorrowerLoanFeaturesPrePayPenaltyTermFalse\",\r\n"
				+ "  \"loanFeaturesPrePayPenaltyTermMonths\": \"EnumBorrowerLoanFeaturesPrePayPenaltyTerm12\",\r\n"
				+ "  \"loanFeaturesInitialBuydownRate\": \"EnumBorrowerLoanFeaturesInitialBuydownRateTrue\",\r\n"
				+ "  \"loanFeaturesInitalBuydownRateMonths\": \"34.567\",\r\n"
				+ "  \"loanFeaturesOther\": \"EnumBorrowerLoanFeaturesOtherFalse\",\r\n"
				+ "  \"serveArmedForces\": \"EnumBorrowerServeArmedForcesTrue\",\r\n"
				+ "  \"borrowerCombineExpenseMonthlyRentalAmount\": 1000,\r\n"
				+ "  \"borrowerCombineExpenseFirstMortgagePIValue\": 200,\r\n"
				+ "  \"borrowerCombineExpenseOtherFinancingPIValue\": 3000,\r\n"
				+ "  \"borrowerCombineExpenseHazardInsuranceValue\": 200,\r\n"
				+ "  \"borrowerCombineExpenseRealEstateTaxesValue\": 1000,\r\n"
				+ "  \"borrowerCombineExpenseMortgageInsuranceValue\": 400,\r\n"
				+ "  \"borrowerCombineExpenseHomeOwnerAssocDuesValue\": 2000,\r\n"
				+ "  \"borrowerCombineExpenseCombineMonthlyExpenseOtherValue\": 1000,\r\n"
				+ "  \"borrowerMonthlyRentalProposedAmount\": 3000,\r\n"
				+ "  \"borrowerFirstMortgagePIProposedValue\": 2000,\r\n"
				+ "  \"borrowerOtherFinancingPIProposedValue\": 1000,\r\n"
				+ "  \"borrowerHazardInsuranceProposedValue\": 2000,\r\n"
				+ "  \"borrowerRealEstateTaxesProposedValue\": 10000,\r\n"
				+ "  \"borrowerMortgageInsuranceProposedValue\": 3000,\r\n"
				+ "  \"borrowerHomeOwnerAssocDuesProposedValue\": 1000,\r\n"
				+ "  \"borrowerCombineMonthlyExpenseOtherProposedValue\": 2000,\r\n"
				+ "  \"noteRatePercent\": 30.678,\r\n" + "  \"constructionImprovementCost\": 120,\r\n"
				+ "  \"originalCostofLot\": 1230,\r\n" + "  \"supplementalPropertyInsuranceProposedValue\": 1230,\r\n"
				+ "  \"borrowerEstimatedMonthlyRent\": 1230,\r\n" + "  \"duefromBrwSalesContractPriceAmt\": 1230,\r\n"
				+ "  \"improvementRenovationandRepairs\": 120,\r\n" + "  \"landIfacquiredSeparately\": 1100,\r\n"
				+ "  \"mortgageBalanceonProperty\": 2230,\r\n" + "  \"creditCardandOtherDebtsOwe\": 3330,\r\n"
				+ "  \"borrowerClosingCost\": 2220,\r\n" + "  \"discountPoints\": 3330,\r\n"
				+ "  \"totalDueFromBorrowers\": 1230,\r\n" + "  \"loanAmountExcludingMortgageInsurance\": 1230,\r\n"
				+ "  \"financedMortgageInsurance\": 1230,\r\n" + "  \"otherNewMortgageLoansonProperty\": 123120,\r\n"
				+ "  \"totalMortgageLoans\": 1230,\r\n" + "  \"sellerCredits\": 1230,\r\n"
				+ "  \"otherCredits\": 1230,\r\n" + "  \"totalCredits\": 1230,\r\n"
				+ "  \"totalCalculationDueFromBorrower\": 1230,\r\n"
				+ "  \"totalCalculationDueMinusFromBorrower\": 1230,\r\n" + "  \"cashFromToBorrower\": 1230,\r\n"
				+ "  \"borrowerCombineMonthlyExpenseTotalAmountCalculator\": 1220,\r\n"
				+ "  \"scheduledFirstPaymentDate\": null,\r\n"
				+ "  \"applicationReceivedDate\": \"2025-03-20T06:40:58.361Z\",\r\n"
				+ "  \"lotAcquired\": \"2000-03-20T06:40:58.361Z\",\r\n" + "  \"borrowerEstimatedValue\": 12310,\r\n"
				+ "  \"titleHeldbyName\": \"string1\",\r\n" + "  \"addressLine1\": null,\r\n"
				+ "  \"addressLine2\": null,\r\n" + "  \"refinanceProgramOtherDescription\": \"string2222222\",\r\n"
				+ "  \"loanFeaturesOtherText\": \"string1111\",\r\n"
				+ "  \"dwellingType\": \"EnumDwellingTypeAttached\"\r\n" + "}";

//		System.out.println(profileUpdateData);
		closeableHttpResponse = restclient.put(url, profileUpdateData, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerPrfile --- " + ststuscode);

		while (ststuscode == 504) {
			closeableHttpResponse = restclient.put(url, profileUpdateData, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerPrfile --- " + ststuscode);

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		} else {
			System.out.println(jsonObj);
		}

		// Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt
		// 200 ok");
	}

	@Test(priority = 4, enabled = true)
	public void UpdateIncome() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/income";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		String IncomeUpdteData = "{\r\n" + "  \"employers\": [\r\n" + "    {\r\n"
				+ "      \"currentlyWork\": \"EnumBorrowerCurrentlyWorkFalse\",\r\n" + "      \"name\": \"emp1\",\r\n"
				+ "      \"title\": \"QC\",\r\n" + "      \"phone\": \"9979977886\",\r\n"
				+ "      \"phoneExt\": \"0123\",\r\n" + "      \"startDate\": \"2000-03-21T05:06:59.337Z\",\r\n"
				+ "      \"endDateIsPresent\": false,\r\n" + "      \"endDate\": \"2010-03-21T05:06:59.337Z\",\r\n"
				+ "      \"workYear\": 10,\r\n" + "      \"workMonths\": 9,\r\n" + "      \"baseSalary\": 4000,\r\n"
				+ "      \"monthlyBonus\": 2000,\r\n" + "      \"commission\": 0,\r\n" + "      \"overtime\": 0,\r\n"
				+ "      \"address\": {\r\n" + "        \"country\": \"US\",\r\n"
				+ "        \"line1\": \"227 Madison Street\",\r\n" + "        \"line2\": \"string\",\r\n"
				+ "        \"city\": \"New York\",\r\n" + "        \"state\": \"New York\",\r\n"
				+ "        \"zipCode\": \"1002\"\r\n" + "      },\r\n" + "      \"otherOptionalIncome\": [\r\n"
				+ "        \"EnumBorrowerOtherOptionalIncomeBonus\"\r\n" + "      ],\r\n"
				+ "      \"commissionIncome\": \"EnumBorrowerEmployerCommissionIncomeFalse\",\r\n"
				+ "      \"borrowerEmployed\": \"EnumBorrowerEmployedTrue\"\r\n" + "    }\r\n" + "  ],\r\n"
				+ "  \"business\": [\r\n" + "    {\r\n"
				+ "      \"currentlyWork\": \"EnumBorrowerCurrentlyWorkTrue\",\r\n"
				+ "      \"monthtlyIncomeAmount\": 2000,\r\n" + "      \"companyName\": \"semp1\",\r\n"
				+ "      \"designation\": \"QAC\",\r\n" + "      \"companyAddress\": {\r\n"
				+ "        \"country\": \"US\",\r\n" + "        \"line1\": \"344 Tully Road\",\r\n"
				+ "        \"line2\": \"string\",\r\n" + "        \"city\": \"Modesto\",\r\n"
				+ "        \"state\": \"California\",\r\n" + "        \"zipCode\": \"95350\"\r\n" + "      },\r\n"
				+ "      \"phone\": \"9989900789\",\r\n" + "      \"phoneExt\": \"445\",\r\n"
				+ "      \"startDate\": \"2017-03-21T05:06:59.337Z\",\r\n" + "      \"endDate\": null,\r\n"
				+ "      \"endDateIsPresent\": true,\r\n"
				+ "      \"own25Percent\": \"EnumBorrowerBusinessOwn25percentTrue\",\r\n"
				+ "      \"businessType\": \"EnumBorrowerBusinessTypeLLC\"\r\n" + "    }\r\n" + "  ],\r\n"
				+ "  \"militaryPay\": 2000,\r\n" + "  \"interestDivident\": null,\r\n" + "  \"rental\": null,\r\n"
				+ "  \"alimonyChild\": {\r\n" + "    \"alimonySupportAmount\": 2000,\r\n"
				+ "    \"alimonySupportStartDate\": \"2000-03-21T05:06:59.337Z\",\r\n"
				+ "    \"businessMonthlyIncomeAmount\": 20000,\r\n" + "    \"alimonyChildren\": [\r\n" + "      {\r\n"
				+ "        \"BorrowerAlimonyChildName\": \"string1child\",\r\n"
				+ "        \"BorrowerAlimonyChildDOB\": \"2010-03-21T05:06:59.337Z\"\r\n" + "      }\r\n" + "    ],\r\n"
				+ "    \"alimonySupport\": {\r\n"
				+ "      \"courtOrdered\": \"EnumBorrowerAlimonySupportCourtOrderedTrue\",\r\n"
				+ "      \"courtReceived\": \"EnumBorrowerAlimonySupportCourtReceivedTrue\",\r\n"
				+ "      \"courtReceiveMore\": \"EnumBorrowerAlimonySupportCourtReceiveMoreFalse\"\r\n" + "    }\r\n"
				+ "  },\r\n" + "  \"socialSecurity\": {\r\n" + "    \"monthlyIncome\": 20000,\r\n"
				+ "    \"startRecieving\": \"2000-03-21T05:06:59.337Z\"\r\n" + "  },\r\n" + "  \"other\": [\r\n"
				+ "    {\r\n" + "      \"perMonth\": 20000,\r\n"
				+ "      \"source\": \"EnumBorrowerOtherIncomeSourceAutomobileAllowance\",\r\n"
				+ "      \"is2OrMoreYears\": \"EnumBorrowerOtherIncomeIs2OrMoreYearsTrue\"\r\n" + "    }\r\n"
				+ "  ]\r\n" + "}";

		closeableHttpResponse = restclient.put(url, IncomeUpdteData, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerIncome --- " + ststuscode);

		while (ststuscode == 504) {
			closeableHttpResponse = restclient.put(url, IncomeUpdteData, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerincome --- " + ststuscode);

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		} else {
			System.out.println(jsonObj);
		}
	}

	@Test(priority = 5, enabled = true)
	public void updatedeclaration() throws ClientProtocolException, IOException {

		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/declaration";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		String DeclarationUpdteData = "{\r\n" + "  \"familyRelationshipwithSellerExplanation\": \"string1\",\r\n"
				+ "  \"otherMortgageLoanonOtherPropertyExplanation\": \"string2\",\r\n"
				+ "  \"newCreditLoanonBeforeClosingExplanation\": \"string3\",\r\n"
				+ "  \"ifLienSubjectExplanation\": \"string4\",\r\n"
				+ "  \"presentlyDelinquentExplanation\": \"string5\",\r\n"
				+ "  \"preForeClosureShortSaleExplanation\": \"string6\",\r\n"
				+ "  \"outstandingJudgementExplanation\": \"string7\",\r\n"
				+ "  \"declaredBankruptExplanation\": \"string8\",\r\n"
				+ "  \"downPaymentBorrowedExplanation\": \"string9\",\r\n"
				+ "  \"partyLawsuitExplanation\": \"string10\",\r\n"
				+ "  \"outstandingJudgement\": \"EnumBorrowerOutstandingJudgementTrue\",\r\n"
				+ "  \"declaredBankrupt\": \"EnumBorrowerDeclaredBankruptTrue\",\r\n"
				+ "  \"propertyForeclosed\": \"EnumBorrowerPropertyForeclosedTrue\",\r\n"
				+ "  \"propertyForeclosedExplanation\": \"string10\",\r\n"
				+ "  \"partyLawsuit\": \"EnumBorrowerPartyLawsuitTrue\",\r\n"
				+ "  \"presentlyDelinquent\": \"EnumBorrowerPresentlyDelinquentTrue\",\r\n"
				+ "  \"downPaymentBorrowed\": \"EnumBorrowerDownPaymentBorrowedTrue\",\r\n"
				+ "  \"endoserOnNote\": \"EnumBorrowerEndoserOnNoteTrue\",\r\n"
				+ "  \"endoserOnNoteExplanation\": \"string11\",\r\n"
				+ "  \"intentToOccupy\": \"EnumBorrowerIntentToOccupyTrue\",\r\n"
				+ "  \"ownshipInterest\": \"EnumBorrowerOwnshipInterestTrue\",\r\n"
				+ "  \"typeOfPropertyOwn\": \"EnumBorrowerTypeOfPropertyOwnFHASR\",\r\n"
				+ "  \"holdTitle\": \"EnumBorrowerHoldTitleBY\",\r\n"
				+ "  \"ethnicityDoNotWish\": \"EnumBorrowerEthnicityDoNotWishTrue\",\r\n"
				+ "  \"hispanicOrLatino\": [\r\n" + "    \"BorrowerHispanicOrLatinoTypeOHL\"\r\n" + "  ],\r\n"
				+ "  \"hispanicOrLatinoTypeOHL\": \"string12\",\r\n"
				+ "  \"hispanicOrLatinoNHL\": \"EnumBorrowerHispanicOrLatinoNHL\",\r\n"
				+ "  \"raceDoNotWish\": \"EnumBorrowerRaceDoNotWishFalse\",\r\n"
				+ "  \"residancy\": \"BorrowerEnrolledPrincipalTribe\",\r\n"
				+ "  \"enrolledPrincipalTribe\": \"string\",\r\n" + "  \"asianType\": [\r\n"
				+ "    \"BorrowerEnterOtherAsianRace\"\r\n" + "  ],\r\n" + "  \"enterOtherAsianRace\": \"string\",\r\n"
				+ "  \"blackOrAfrician\": \"EnumBorrowerResidancyBAA\",\r\n" + "  \"residancyNH\": [\r\n"
				+ "    \"BorrowerEnterOtherPacificIslanderRace\"\r\n" + "  ],\r\n"
				+ "  \"residancyWH\": \"EnumBorrowerResidancyWH\",\r\n"
				+ "  \"enterOtherPacificIslanderRace\": \"string\",\r\n"
				+ "  \"sexDoNotWish\": \"EnumBorrowerSexDoNotWishFalse\",\r\n" + "  \"maleFemale\": [\r\n"
				+ "    \"EnumBorrowerMaleFemaleFemale\"\r\n" + "  ],\r\n"
				+ "  \"familyRelationshipwithSeller\": \"EnumDeclarationFamilyRelationshipwithSellerTrue\",\r\n"
				+ "  \"otherMortgageLoanonOtherProperty\": \"EnumDeclarationOtherMortgageLoanonOtherPropertyTrue\",\r\n"
				+ "  \"newCreditLoanonBeforeClosing\": \"EnumDeclarationNewCreditLoanonBeforeClosingTrue\",\r\n"
				+ "  \"ifLienSubject\": \"EnumDeclarationIfLienSubjectTrue\",\r\n"
				+ "  \"propertyForeclosedConveyedTitle\": \"EnumBorrowerPropertyForeclosedConveyedTitleTrue\",\r\n"
				+ "  \"preForeClosureShortSale\": \"EnumDeclarationPreForeClosureShortSaleTrue\",\r\n"
				+ "  \"declaredBankruptChapter\": [\r\n" + "    \"EnumBorrowerDeclaredBankruptChapter11\"\r\n"
				+ "  ],\r\n" + "  \"citizenship\": \"EnumBorrowerNonPermanentResidentAlien\",\r\n"
				+ "  \"downPaymentBorrowedAmount\": \"3000\",\r\n"
				+ "  \"propertyForeclosedConveyedTitleExplanation\": \"string13\",\r\n"
				+ "  \"ethnicityCollected\": \"EnumBorrowerEthnicityCollectedTrue\",\r\n"
				+ "  \"raceCollected\": \"EnumBorrowerRaceCollectedFalse\",\r\n"
				+ "  \"sexCollected\": \"EnumBorrowerSexCollectedTrue\",\r\n"
				+ "  \"demographicFace\": \"EnumBorrowerDemographicFace\",\r\n"
				+ "  \"demographicTelephone\": \"EnumBorrowerDemographicTelephone\",\r\n"
				+ "  \"demographicFax\": \"EnumBorrowerDemographicFax\",\r\n"
				+ "  \"demographicEmail\": \"EnumBorrowerDemographicEmail\"\r\n" + "}";

		closeableHttpResponse = restclient.put(url, DeclarationUpdteData, headermap);
		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of updateBorrowerDeclaration --- " + ststuscode);

		while (ststuscode == 504) {
			closeableHttpResponse = restclient.put(url, DeclarationUpdteData, headermap);
			ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		}
		System.out.println("Status code of updateBorrowerDeclaration --- " + ststuscode);

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println(jsonObj);
		} else {
			System.out.println(jsonObj);
		}
	}

	@Test(priority = 6, enabled = true)
	public void getBorrowerPrfile() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/profile";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerPrfile --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 7, enabled = true)
	public void getBorrowerIncome() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/income";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerIncome --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 8, enabled = true)
	public void getBorrowerAsset() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/asset";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerAsset --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 9, enabled = true)
	public void getBorrowerLiabilities() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v2/loans/" + loanid + "/borrower/" + email + "/liabilities";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerLiabilities --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 10, enabled = true)
	public void getBorrowerDeclaration() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid + "/borrower/" + email + "/declaration";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerDeclaration --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 8, enabled = false)
	public void getApplicantsByLoanId() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid + "/borrower";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicantsByLoanId --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);

	}

	@Test(priority = 11, enabled = true)
	public void getLoansByBorrowerUsername() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/" + email + "/loans";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoansByBorrowerUsername --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jarray = new JSONArray(responseString);

		JSONObject jsonObj = new JSONObject(jarray);
		System.out.println("Get Profile endpoin response : " + jarray);
	}

	@Test(priority = 12, enabled = false)
	public void getBorrowerAcountDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid + "/borrower/" + email + "/bankaccounts";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerAcountDetails --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);
	}

	@Test(priority = 13, enabled = true)
	public void getBorrowerByUsername() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/" + email;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerByUsername --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);
	}

	@Test(priority = 14, enabled = true)
	public void getLoanLogs() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid + "/log";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanLogs --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONArray jsonObj = new JSONArray(responseString);
		System.out.println("Get LoanLogs endpoin response : " + jsonObj);
	}

	@Test(priority = 15, enabled = true)
	public void getAllLoanDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/all";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllLoanDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get AllLoanDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			JSONArray jarray = new JSONArray(responseString);
			System.out.println("Get AllLoanDetails endpoin response : " + jarray);
		}

	}

	@Test(priority = 16, enabled = true)
	public void getAllActiveLoanDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/active";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllActiveLoanDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllActiveLoanDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get AllActiveLoanDetails endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 17, enabled = true)
	public void getAllApplicationsDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllApplicationsDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllApplicationsDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get AllApplicationsDetails endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 18, enabled = true)
	public void getAllCLosedLoansDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/closed";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllCLosedLoansDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllCLosedLoansDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get AllCLosedLoansDetails endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 19, enabled = true)
	public void getLoandata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoandata --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get Loandata endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get Loandata endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 20, enabled = true)
	public void getApplicationdata() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/application/" + loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicationdata --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get Applicationdata endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get Applicationdata endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 21, enabled = true)
	public void getSummaryOfLoan() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + loanid + "/borrower/" + email + "/summary";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getSummaryOfLoan --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get SummaryOfLoan endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get SummaryOfLoan endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 22, enabled = true)
	public void getLoaninReviewStatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications/reviewLoans/mine";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoaninReviewStatus --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoaninReviewStatus endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get LoaninReviewStatus endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 23, enabled = true)
	public void getAllLoaninReviewStatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/applications/reviewLoans/all";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllLoaninReviewStatus --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get AllLoaninReviewStatus endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get AllLoaninReviewStatus endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 24, enabled = true)
	public void getBorrowerLogs() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/" + email + "/log";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerLogs --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerLogs endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get BorrowerLogs endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 25, enabled = true)
	public void getBorrowerList() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrowers";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerList --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerList endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get BorrowerList endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 26, enabled = true)
	public void getBorrowerProfileDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/borrower/" + email;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBorrowerProfileDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BorrowerProfileDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get BorrowerProfileDetails endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 27, enabled = true)
	public void getLoanOfficerList() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficers";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanOfficerList --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoanOfficerList endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get LoanOfficerList endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 28, enabled = true)
	public void getLoanOfficerDetails() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/" + users.getUsername();
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoanOfficerDetails --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoanOfficerDetails endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get LoanOfficerDetails endpoin response : " + jsonObj);
		}

		LOdetailsVerification lodetailsVerification = new LOdetailsVerification();
		lodetailsVerification.responsevalidation(jsonObj);
	}

	@Test(priority = 29, enabled = true)
	public void getLoansOfLoanofficer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/" + users.getUsername() + "/loans";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getLoansOfLoanofficer --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONArray jsonObj = new JSONArray(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get LoansOfLoanofficer endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get LoansOfLoanofficer endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 30, enabled = true)
	public void getBrandedCodeOfLoanofficer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loanofficer/" + users.getUsername() + "/branded";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getBrandedCodeOfLoanofficer --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			System.out.println("Get BrandedCodeOfLoanofficer endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get BrandedCodeOfLoanofficer endpoin response : " + jsonObj);
		}
	}

	@Test(priority = 31, enabled = true)
	public void RedirectToSummaryScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/" + loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		ObjectMapper mapper = new ObjectMapper();
		users = new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);

		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of RedirectToSummaryScreen --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToSummaryScreen endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get RedirectToSummaryScreen endpoin response : " + responseString);
		}
	}

	@Test(priority = 12, enabled = false)
	public void getApplicationData() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/application/" + loanid;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getApplicationData --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);
	}

	@Test(priority = 13, enabled = false)
	public void getreadFullLoanContainer() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/test/loan/" + loanid + "/readFullLoanContainer";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getreadFullLoanContainer --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);
	}

	@Test(priority = 14, enabled = false)
	public void UpdateLoanstatus() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/UpdateStatus";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		String body = "{\r\n" + "	\"status\": \"EnumLoanStatusProcessing\"\r\n" + "} ";

		closeableHttpResponse = restclient.put(url, body, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of UpdateLoanstatus --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("Get Profile endpoin response : " + jsonObj);
	}

	@Test(priority = 15, enabled = false)
	public void getAllConditionsDocuments() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/documents";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllConditionsDocuments --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONArray jsonArray = new JSONArray(responseString);
		JSONObject jObj = jsonArray.getJSONObject(0);
		System.out.println("getAllConditionsDocuments endpoin response : " + jObj);
		DocumetnID = jObj.getString("id");

	}

	@Test(priority = 16, enabled = false)
	public void getPerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/document/" + DocumetnID;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getPerticularConditionsDocument --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("getPerticularConditionsDocument endpoin response : " + jsonObj);

	}

	@Test(priority = 17, enabled = false)
	public void DownloadPerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/document/" + DocumetnID;
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of DownloadPerticularConditionsDocument --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("DownloadPerticularConditionsDocument endpoin response : " + jsonObj);

	}

	@Test(priority = 18, enabled = false)
	public void DeletePerticularConditionsDocument() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/" + DocumetnID + "/DeleteDocument";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.delete(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of DownloadPerticularConditionsDocument --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("DownloadPerticularConditionsDocument endpoin response : " + jsonObj);

	}

	@Test(priority = 19, enabled = false)
	public void getAllCondition() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/conditions";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		closeableHttpResponse = restclient.get(url, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of getAllCondition --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONArray jsonArray = new JSONArray(responseString);
		JSONObject jObj = jsonArray.getJSONObject(1);
		System.out.println("getAllConditionsDocuments endpoin response : " + jObj);
		DocumetnID = jObj.getString("id");

	}

	@Test(priority = 20, enabled = false)
	public void SubmitCondition() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/loans/" + closedloanid + "/conditions";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "multipart/form-data");
		headermap.put("Authorization", token);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("explanation", "app_user"));

		closeableHttpResponse = restclient.postwihformdata(url, urlParameters, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of SubmitCondition --- " + ststuscode);
		Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");

		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONObject jsonObj = new JSONObject(responseString);
		System.out.println("SubmitCondition endpoin response : " + jsonObj);

	}

	@Test(priority = 32, enabled = true)
	public void RedirectToDocumentScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/" + loanid + "/documents";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		ObjectMapper mapper = new ObjectMapper();
		users = new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);

		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of RedirectToDocumentScreen --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToDocumentScreen endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get RedirectToDocumentScreen endpoin response : " + responseString);
		}
	}

	@Test(priority = 33, enabled = true)
	public void RedirectToConditionsScreen() throws ClientProtocolException, IOException {
		String url = Envurl + "/api/v1/jwt/redirect/" + loanid + "/conditions";
		restclient = new restClient();
		HashMap<String, String> headermap = new HashMap<String, String>();
		headermap.put("Content-Type", "application/json");
		headermap.put("Authorization", token);

		ObjectMapper mapper = new ObjectMapper();
		users = new users();
		mapper.writeValue(new File("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\qa\\data\\User.json"), users);
		String UserJsonString = mapper.writeValueAsString(users);

		closeableHttpResponse = restclient.post(url, UserJsonString, headermap);

		int ststuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code of RedirectToConditionsScreen --- " + ststuscode);
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		if (ststuscode != STATUS_RESPONSE_CODE_200) {
			JSONObject jsonObj = new JSONObject(responseString);
			System.out.println("Get RedirectToConditionsScreen endpoin response : " + jsonObj);
			Assert.assertEquals(ststuscode, STATUS_RESPONSE_CODE_200, "status code is nt 200 ok");
		} else {
			System.out.println("Get RedirectToConditionsScreen endpoin response : " + responseString);
		}
	}

}
