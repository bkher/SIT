package validations;

import static org.testng.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qa.utils.testUtils;

public class LOdetailsVerification {

	testUtils utilitobj;
	public void responsevalidation(JSONObject obj) {
		
		assertEquals(utilitobj.getvlueByJpath(obj, "nmlsNumber"), "12345678", "nmlsNumber is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "brandedUniqueCode"), "79f134031ade45f5947e0e942f1e184d", "brandedUniqueCode is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "loObjectId"), "5666959", "loobjectId is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "userName"), "docittlender@gmail.com", "userName is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "firstName"), "Jerry", "firstName is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "lastName"), "Johnson", "lastName is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "title"), "Loan Officer", "title is not same as mentioned");
		
		assertEquals(utilitobj.getvlueByJpath(obj, "mobileNumber"), "1234567890", "mobileNumber is not same as mentioned");
		
		String address = utilitobj.getvlueByJpath(obj, "address");
		JSONObject Addressjson = new JSONObject(address);
		assertEquals(utilitobj.getvlueByJpath(Addressjson, "address"), "3324 West Street", "address line 1 is not same as mentioned");
		assertEquals(utilitobj.getvlueByJpath(Addressjson, "city"), "Las Vegas", "city is not same as mentioned");
		assertEquals(utilitobj.getvlueByJpath(Addressjson, "state"), "CA", "state is not same as mentioned");
		assertEquals(utilitobj.getvlueByJpath(Addressjson, "zip"), "98765", "zip is not same as mentioned");
		
		
		String com = utilitobj.getvlueByJpath(obj, "communication");
		JSONArray jarray = new JSONArray(com);
		assertEquals(jarray.get(0), "EnumCommunicationEmail", "mail is not in communicate array");		
		assertEquals(jarray.get(1), "EnumCommunicationPhone", "Phone is not in communicate array");
		assertEquals(jarray.get(2), "EnumCommunicationText", "Text not in communicate array");
		
	}
	
	
}
