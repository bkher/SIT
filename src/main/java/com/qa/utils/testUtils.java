package com.qa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class testUtils {

	
	public static String getvlueByJpath(JSONObject responsejson,String Jpath) {
		
		Object obj = responsejson;
		for (String s : Jpath.split("/"))
			if(!s.isEmpty())
				if(!(s.contains("[") || s.contains("]")))
					obj = ((JSONObject) obj).get(s);
				else if (!(s.contains("[") || s.contains("]")))
					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0])).get(Integer.parseInt(s.split("\\[")[1].replaceAll("]", "")));
		return obj.toString();
	}
	
	
	public static String generateRandomEmail() { 
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
	    String date = simpleDateFormat.format(new Date());
	    String email = "test+" + date + "@gmail.com";
	    return email;
	}
	
}
