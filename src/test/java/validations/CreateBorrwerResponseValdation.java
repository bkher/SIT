package validations;

import static org.testng.Assert.assertTrue;

import org.json.JSONObject;

import com.qa.utils.testUtils;

public class CreateBorrwerResponseValdation {

	testUtils utilitobj;

	public void responsevalidation(JSONObject obj) {

		String profile = utilitobj.getvlueByJpath(obj, "profile");
		JSONObject profileJson = new JSONObject(profile);

		assertTrue(utilitobj.getvlueByJpath(profileJson, "directSignInToken") != null,
				"directSignInToken should not null");
		assertTrue(utilitobj.getvlueByJpath(profileJson, "directSignInUrl") != null, "directSignInUrl should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "loanId") != null, "loanId should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "transactionType") != null, "transactionType should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "firstName") != null, "firstName should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "lastName") != null, "lastName should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "preferredEmail") != null, "preferredEmail should not null");

		assertTrue(utilitobj.getvlueByJpath(profileJson, "cellPhone") != null, "cellPhone should not null");

		assertTrue(utilitobj.getvlueByJpath(obj, "loanId") != null, "loanId should not null");
	}
}
