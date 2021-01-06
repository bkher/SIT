package com.qa.data;

public class CreateLoansUsingInvitationToken {
	
	String purpose;
	String invitationToken;
	String email;
	String password;
	public CreateLoansUsingInvitationToken() {
		
	}

	public CreateLoansUsingInvitationToken(String purpose,String invitationToken,String email,String password) {
		this.purpose = purpose;
		this.invitationToken = invitationToken;
		this.email = email;
		this.password = password;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getInvitationToken() {
		return invitationToken;
	}

	public void setInvitationToken(String invitationToken) {
		this.invitationToken = invitationToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
