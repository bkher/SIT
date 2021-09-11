package TestData;

public class IncomeData {

	
	public String IncomePayload() {
		
		String IncomeUpdteData = "{\r\n" + 
				"  \"employers\": [\r\n" + 
				"    {\r\n" + 
				"      \"currentlyWork\": \"EnumBorrowerCurrentlyWorkFalse\",\r\n" + 
				"      \"name\": \"emp1\",\r\n" + 
				"      \"title\": \"QC\",\r\n" + 
				"      \"phone\": \"9979977886\",\r\n" + 
				"      \"phoneExt\": \"0123\",\r\n" + 
				"      \"startDate\": \"2000-03-21T05:06:59.337Z\",\r\n" + 
				"      \"endDateIsPresent\": false,\r\n" + 
				"      \"endDate\": \"2010-03-21T05:06:59.337Z\",\r\n" + 
				"      \"workYear\": 10,\r\n" + 
				"      \"workMonths\": 9,\r\n" + 
				"      \"baseSalary\": 4000,\r\n" + 
				"      \"monthlyBonus\": 2000,\r\n" + 
				"      \"commission\": 0,\r\n" + 
				"      \"overtime\": 0,\r\n" + 
				"      \"address\": {\r\n" + 
				"        \"country\": \"US\",\r\n" + 
				"        \"line1\": \"227 Madison Street\",\r\n" + 
				"        \"line2\": \"string\",\r\n" + 
				"        \"city\": \"New York\",\r\n" + 
				"        \"state\": \"New York\",\r\n" + 
				"        \"zipCode\": \"1002\"\r\n" + 
				"      },\r\n" + 
				"      \"otherOptionalIncome\": [\r\n" + 
				"        \"EnumBorrowerOtherOptionalIncomeBonus\"\r\n" + 
				"      ],\r\n" + 
				"      \"commissionIncome\": \"EnumBorrowerEmployerCommissionIncomeFalse\",\r\n" + 
				"      \"borrowerEmployed\": \"EnumBorrowerEmployedTrue\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"business\": [\r\n" + 
				"    {\r\n" + 
				"      \"currentlyWork\": \"EnumBorrowerCurrentlyWorkTrue\",\r\n" + 
				"      \"monthtlyIncomeAmount\": 2000,\r\n" + 
				"      \"companyName\": \"semp1\",\r\n" + 
				"      \"designation\": \"QAC\",\r\n" + 
				"      \"companyAddress\": {\r\n" + 
				"        \"country\": \"US\",\r\n" + 
				"        \"line1\": \"344 Tully Road\",\r\n" + 
				"        \"line2\": \"string\",\r\n" + 
				"        \"city\": \"Modesto\",\r\n" + 
				"        \"state\": \"California\",\r\n" + 
				"        \"zipCode\": \"95350\"\r\n" + 
				"      },\r\n" + 
				"      \"phone\": \"9989900789\",\r\n" + 
				"      \"phoneExt\": \"445\",\r\n" + 
				"      \"startDate\": \"2017-03-21T05:06:59.337Z\",\r\n" + 
				"      \"endDate\": null,\r\n" + 
				"      \"endDateIsPresent\": true,\r\n" + 
				"      \"own25Percent\": \"EnumBorrowerBusinessOwn25percentTrue\",\r\n" + 
				"      \"businessType\": \"EnumBorrowerBusinessTypeLLC\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"militaryPay\": 2000,\r\n" + 
				"  \"interestDivident\": null,\r\n" + 
				"  \"rental\": null,\r\n" + 
				"  \"alimonyChild\": {\r\n" + 
				"    \"alimonySupportAmount\": 2000,\r\n" + 
				"    \"alimonySupportStartDate\": \"2000-03-21T05:06:59.337Z\",\r\n" + 
				"    \"businessMonthlyIncomeAmount\": 20000,\r\n" + 
				"    \"alimonyChildren\": [\r\n" + 
				"      {\r\n" + 
				"        \"BorrowerAlimonyChildName\": \"string1child\",\r\n" + 
				"        \"BorrowerAlimonyChildDOB\": \"2010-03-21T05:06:59.337Z\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"alimonySupport\": {\r\n" + 
				"      \"courtOrdered\": \"EnumBorrowerAlimonySupportCourtOrderedTrue\",\r\n" + 
				"      \"courtReceived\": \"EnumBorrowerAlimonySupportCourtReceivedTrue\",\r\n" + 
				"      \"courtReceiveMore\": \"EnumBorrowerAlimonySupportCourtReceiveMoreFalse\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  \"socialSecurity\": {\r\n" + 
				"    \"monthlyIncome\": 20000,\r\n" + 
				"    \"startRecieving\": \"2000-03-21T05:06:59.337Z\"\r\n" + 
				"  },\r\n" + 
				"  \"other\": [\r\n" + 
				"    {\r\n" + 
				"      \"perMonth\": 20000,\r\n" + 
				"      \"source\": \"EnumBorrowerOtherIncomeSourceAutomobileAllowance\",\r\n" + 
				"      \"is2OrMoreYears\": \"EnumBorrowerOtherIncomeIs2OrMoreYearsTrue\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		
		return IncomeUpdteData;
	}
}
