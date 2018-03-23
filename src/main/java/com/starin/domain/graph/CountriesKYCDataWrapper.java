package com.starin.domain.graph;

import com.starin.utils.ObjectMap;

public class CountriesKYCDataWrapper {
	String kycVerified;
	String kycUnverified;
	String countryName;
	Integer countryId;

	public String toString() {
		return ObjectMap.objectMap(this).toString();
	}

	public CountriesKYCDataWrapper(String countryName, String kycverified, String kycunverified, Integer countryId) {
		this.countryName = countryName;
		this.kycUnverified = kycunverified;
		this.kycVerified = kycverified;
		this.countryId = countryId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getKycVerified() {
		return kycVerified;
	}

	public void setKycVerified(String kycVerified) {
		this.kycVerified = kycVerified;
	}

	public String getKycUnverified() {
		return kycUnverified;
	}

	public void setKycUnverified(String kycUnverified) {
		this.kycUnverified = kycUnverified;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
