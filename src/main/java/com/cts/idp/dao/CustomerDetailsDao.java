package com.cts.idp.dao;

public class CustomerDetailsDao {
	
	private String userId;
	private String firstName;
	private String lastName;
	private String middleName;
	private String dob;
	private String address;
	

	
	public CustomerDetailsDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerDetailsDao(String userId, String firstName, String lastName, String middleName, String dob, String address) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.dob = dob;
		this.address = address;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "CustomerDetails [firstName=" + firstName + ", lastName=" + lastName + ", middleName=" + middleName
				+ ", dob=" + dob + ", address=" + address + "]";
	}

	
}
