package com.qa.data;

public class InviteUserData {
	
	String firstName;
	String lastName;
	String email;
	String mobilenumber;
	
	public String getFirstName() {
		return firstName;
	}
	
	public InviteUserData(String firstName,String lastName,String email,String mobilenumber) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobilenumber = mobilenumber;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public InviteUserData() {
		
	}
	
}
