package com.starin.domain.user;

import javax.validation.constraints.Pattern;

public class Req_Address {

	/*
	 * Value Type object Of Address
	 */
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;

	@Pattern(regexp = "[0-9]+", message = "phone should be Integer")
	private String phone;

	@Pattern(regexp = "[0-9]+", message = "pincode should be Integer")
	private String pincode;

	public Req_Address() {
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

}
