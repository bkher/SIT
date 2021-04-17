package validations;

import static org.testng.Assert.assertTrue;

import org.json.JSONObject;

import com.qa.utils.testUtils;

public class GetBorrwerProfileResponseValdation {

	testUtils utilitobj;

	public void responsevalidation(JSONObject obj) {

		assertTrue(utilitobj.getvlueByJpath(obj, "loanId") != null, "loanId should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "transactionType") != null, "transactionType should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "firstName") != null, "firstName should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "lastName") != null, "lastName should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "borrowerBirthDate") != null, "borrowerBirthDate should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "ssn") != null, "ssn should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "preferredEmail") != null, "preferredEmail should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "cellPhone") != null, "cellPhone should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "selectedProperty") != null, "selectedProperty should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "propertyType") != null, "propertyType should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "propertyUse") != null, "propertyUse should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "city") != null, "city should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "zipCode") != null, "zipCode should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "state") != null, "state should not null");

		String loanAmount = utilitobj.getvlueByJpath(obj, "loanAmount");
		JSONObject loanAmountjson = new JSONObject(loanAmount);

		assertTrue(utilitobj.getvlueByJpath(loanAmountjson, "purchasePrice") != null, "purchasePrice should not null");
		assertTrue(utilitobj.getvlueByJpath(loanAmountjson, "downPayment") != null, "downPayment should not null");
		assertTrue(utilitobj.getvlueByJpath(loanAmountjson, "loanPercentage") != null,
				"loanPercentage should not null");
		assertTrue(utilitobj.getvlueByJpath(loanAmountjson, "loanAmount") != null, "loanAmount should not null");

		assertTrue(utilitobj.getvlueByJpath(obj, "individualOrJointCredit") != null,
				"individualOrJointCredit should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "maritalStatus") != null, "maritalStatus should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "addSpouseAsCoBorrower") != null,
				"addSpouseAsCoBorrower should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "vaEligible") != null, "vaEligible should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "vaSelectOptions") != null, "vaSelectOptions should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "existingVaLoan") != null, "existingVaLoan should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "monthlyChildCareVa") != null, "monthlyChildCareVa should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "monthlyChildCareMoneyVa") != null,
				"monthlyChildCareMoneyVa should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "realEstateAgent") != null, "realEstateAgent should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "realEstateReferral") != null, "realEstateReferral should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "rentOrOwn") != null, "rentOrOwn should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "currentAddressSame") != null, "currentAddressSame should not null");
		String currentStreetAddress = utilitobj.getvlueByJpath(obj, "currentStreetAddress");
		JSONObject currentStreetAddressjson = new JSONObject(currentStreetAddress);
		assertTrue(utilitobj.getvlueByJpath(currentStreetAddressjson, "country") != null, "country should not null");
		assertTrue(utilitobj.getvlueByJpath(currentStreetAddressjson, "line1") != null, "line1 should not null");
		assertTrue(utilitobj.getvlueByJpath(currentStreetAddressjson, "city") != null, "city should not null");
		assertTrue(utilitobj.getvlueByJpath(currentStreetAddressjson, "state") != null, "state should not null");
		assertTrue(utilitobj.getvlueByJpath(currentStreetAddressjson, "zipCode") != null, "zipCode should not null");

		assertTrue(utilitobj.getvlueByJpath(obj, "currentAddressStartDate") != null,
				"currentAddressStartDate should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "differentMailingAddress") != null,
				"differentMailingAddress should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "lookingToSell") != null, "lookingToSell should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "dependentsNumber") != null, "dependentsNumber should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "loanFeatures") != null, "loanFeatures should not null");

	}
}
