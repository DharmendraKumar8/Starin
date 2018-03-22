package com.starin.domain.documents;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.starin.domain.generalfield.UUIDField;
import com.starin.domain.user.UserCountry;
import com.starin.utils.ObjectMap;


@Entity
public class Country extends UUIDField{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer countryID;

	private String countryCode;

	@Column(length=100)
	private String countryDialCode;

	@NotNull
	@Column(unique=true)
	private String countryName;

	@OneToMany(mappedBy="country")
	private Set<UserCountry> users = new LinkedHashSet<UserCountry>() ;
	
	public Country(){
		super(UUID.randomUUID().toString());
	}

	public Country(String countryCode,String countryName){
		super(UUID.randomUUID().toString());
		this.countryCode = countryCode;
		this.countryName = countryName;
	}

	public Set<UserCountry> getUsers() {
		return users;
	}

	public void setUsers(Set<UserCountry> users) {
		this.users = users;
	}

	public Integer getCountryID() {
		return countryID;
	}

	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode.trim();
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName.trim();
	}

	public String getCountryDialCode() {
		return countryDialCode;
	}

	public void setCountryDialCode(String countryDialCode) {
		this.countryDialCode = countryDialCode;
	}
	
	public Map<String,Object> getCountryMap(){
		Map<String,Object> result = ObjectMap.objectMap(this,"countryCode~countryName~countryDialCode");
		result.put("countryId", this.countryID);
		return result;
	}

}
