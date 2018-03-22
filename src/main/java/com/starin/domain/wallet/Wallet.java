package com.starin.domain.wallet;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.starin.domain.documents.Country;
import com.starin.domain.generalfield.UUIDField;
import com.starin.domain.user.User;
import com.starin.enums.WalletStatus;
import com.starin.utils.ObjectHash;



@Entity
@Table(name="wallet",uniqueConstraints= @UniqueConstraint(columnNames={"u_id", "country_id"}))
public class Wallet extends UUIDField{
	
	/*
	 * wallet_id is the primary key 
	 */	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="wallet_id")
	int wallet_id;
	
	@Column(name="wallet_address")
	private String address;
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="wallet_status")
	private WalletStatus status=null;

	@ManyToOne
	@JoinColumn(name="u_id")
	private User user;
	
	@OneToOne
	@JoinColumn(name="country_id")
	@ObjectHash
	private Country country;

	
	private boolean emailSended;
	


	public boolean isEmailSended() {
		return emailSended;
	}
    
	public void setEmailSended(boolean emailSended) {
		this.emailSended = emailSended;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	public Wallet(){super(UUID.randomUUID().toString());}
	
	public Wallet(WalletStatus walletStatus,User user,Country country){
		super(UUID.randomUUID().toString());
		this.status = walletStatus;
		this.user = user;
		this.country=country;
	}
	
	public int getWallet_id() {
		return wallet_id;
	}

	public void setWallet_id(int wallet_id) {
		this.wallet_id = wallet_id;
	}

	public WalletStatus getStatus() {
		return status;
	}

	public void setStatus(WalletStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ObjectHash
	public String status(){
		return status.toString();
	}
}
