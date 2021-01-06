package com.qa.data;

public class users {

	String Tenant="naf";
	String username="docittlender@gmail.com";
	String password="Jan@2017";
	
	
	public users() {

	}	

	/*
	 * public users(String Tenant,String username, String password) {
	 * 
	 * this.Tenant= Tenant; this.username= username; this.password = password; }
	 */
	
	public String getTenant() {
		return Tenant;
	}

	public void setTenant(String tenant) {
		Tenant = tenant;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
