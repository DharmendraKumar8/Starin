package com.starin.domain.user;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.starin.domain.documents.Country;
import com.starin.domain.generalfield.UUIDField;



@Entity
@Table(name="user_country",
indexes={@Index(columnList = "kyc_date", name = "Idx_UserCountry_kycDate") }
)
public class UserCountry extends UUIDField{

	@EmbeddedId
	private UserCountryId userCountryId;

	@ManyToOne
	@JoinColumn(name="userId",updatable = false,insertable=false)
	private User user;

	@ManyToOne
	@JoinColumn(name="countryId",updatable = false,insertable=false)
	private Country country;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="kyc_date")
	private Date kycDate; 

	public Date getKycDate() {
		return kycDate;
	}

	public void setKycDate(Date kycDate) {
		this.kycDate = kycDate;
	}

	private boolean kycVerificationStatus=false;

	public boolean isKycVerificationStatus() {
		return kycVerificationStatus;
	}

	public void setKycVerificationStatus(boolean kycVerificationStatus) {
		this.kycVerificationStatus = kycVerificationStatus;
	}

	public UserCountryId getUserCountryId() {
		return userCountryId;
	}

	public void setUserCountryId(UserCountryId userCountryId) {
		this.userCountryId = userCountryId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public UserCountry(){
		super(UUID.randomUUID().toString());
	}

	public UserCountry(User user, Country country) {
		super(UUID.randomUUID().toString());
		// create primary key
		this.userCountryId = new UserCountryId(user.uid, country.getCountryID());

		// initialize attributes
		this.user = user;
		this.country = country;

		// update relationships to assure referential integrity
		user.getCountries().add(this);
		country.getUsers().add(this);
		
	}

}
