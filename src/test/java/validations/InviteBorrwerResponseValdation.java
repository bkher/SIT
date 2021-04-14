package validations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.json.JSONObject;

import com.qa.utils.testUtils;

public class InviteBorrwerResponseValdation {

	testUtils utilitobj;

	public void responsevalidation(JSONObject obj) {

		assertTrue(utilitobj.getvlueByJpath(obj, "id") != null, "ID should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "invitedBy") != null, "invitedBy should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "invitationDate") != null, "invitationDate should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "invitationAcceptedDate") != null,
				"invitationAcceptedDate should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "invitationUrl") != null, "invitationUrl should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "email") != null, "email should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "firstName") != null, "firstName should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "lastName") != null, "lastName should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "mobileNumber") != null, "mobileNumber should not null");
		assertTrue(utilitobj.getvlueByJpath(obj, "invitationToken") != null, "invitationToken should not null");
		assertEquals(utilitobj.getvlueByJpath(obj, "role"), "Borrower", "role is not same as mentioned");

		assertTrue(utilitobj.getvlueByJpath(obj, "invitationTokenExpiry") != null,
				"invitationTokenExpiry should not null");

	}
}
