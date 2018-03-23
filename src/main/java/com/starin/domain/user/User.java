package com.starin.domain.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.starin.domain.documents.KYCUserDocument;
import com.starin.domain.generalfield.UUIDField;
import com.starin.domain.role.Role;
import com.starin.domain.wallet.Wallet;
import com.starin.utils.KYCUtilities;
import com.starin.utils.ObjectHash;
import com.starin.utils.ObjectMap;




@Entity
@Table(name="user",
indexes={@Index(columnList = "default_country_kycdate", name = "Idx_User_defaultCountryKYCDate") }
)
public class User extends UUIDField{
	/*
	 * uid is the primary key
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="u_id")
	Integer uid;

	@Column(name="email",unique=true)
	@NotNull(message="email can not be null")
	@Size(min=1,message="email can not be empty")
	private String email;

	@NotNull(message="password can not be null")
	@Column(name="password")
	private String password;

	@Column(name="registerDate")
	private Date registeredDate;

	@NotNull(message="username can not be null")
	@Column(name="username")
	private String username;	

	@Column(name="isemailverified" )
	private boolean isEmailVerified=false;

	@Column(name="isKYC_Verified" )
	private boolean isKYCVerified=false;

	@NotNull(message="name can not be null")
	@Column(name="name")	
	private String name;		
	
	@Column	private boolean firstLoginAttempt=false;
	
	@Column	private Integer secretKeyRequestCount=0;	

	public boolean isFirstLoginAttempt() {
		return firstLoginAttempt;
	}

	public void setFirstLoginAttempt(boolean firstLoginAttempt) {
		this.firstLoginAttempt = firstLoginAttempt;
	}

	public Integer getSecretKeyRequestCount() {
		return secretKeyRequestCount;
	}

	public void setSecretKeyRequestCount(Integer secretKeyRequestCount) {
		this.secretKeyRequestCount = secretKeyRequestCount;
	}

	@Column(name="isActive")	
	private boolean isActive=false;

	private String profileImagePath;

	private Short defaultCountryId;

	@Column(columnDefinition="json")
	private String socialMeta="{}";

	//Hyperledger public key
	private String hPublicKey;
	
	@ObjectHash
	@Embedded
	private Address address;
	
	public String gethPublicKey() {
		return hPublicKey;
	}

	public void sethPublicKey(String hPublicKey) {
		this.hPublicKey = hPublicKey;
	}

	public Date getDefaultCountryKYCDate() {
		return defaultCountryKYCDate;
	}

	public void setDefaultCountryKYCDate(Date defaultCountryKYCDate) {
		this.defaultCountryKYCDate = defaultCountryKYCDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="default_country_kycdate")
	private Date defaultCountryKYCDate;

	public Short getDefaultCountryId() {
		return defaultCountryId;
	}

	public void setDefaultCountryId(Short defaultCountryId) {
		this.defaultCountryId = defaultCountryId;
	}

	@ObjectHash
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "roleid", nullable = false)
	private Role role;

	@OneToMany(mappedBy="user",fetch=FetchType.LAZY)
	private List<Wallet> wallets=new LinkedList<Wallet>();



	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	@OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
	List<LoginHistory> loginHistoryArray = new ArrayList<LoginHistory>();

	@OneToMany(mappedBy = "user")
	List<KYCUserDocument> kycUserDocumentArray = new ArrayList<KYCUserDocument>();

	@OneToMany(mappedBy="user")
	private Set<UserCountry> countries = new LinkedHashSet<UserCountry>() ;

	public User(){
		super(UUID.randomUUID().toString());
		this.defaultCountryKYCDate=new Date();
	}

	public Set<UserCountry> getCountries() {
		return countries;
	}

	public void setCountries(Set<UserCountry> countries) {
		this.countries = countries;
	}

	public User(String name,String password,String email,Role role,Date createdAt,Address address,Short defaultCountryId){
		super(UUID.randomUUID().toString());
		this.name=name;	
		this.password=password;
		this.email=email;		
		this.role=role;		
		this.username=email;	
		this.registeredDate=createdAt;
		this.address = address;
		this.defaultCountryId = defaultCountryId;
		this.defaultCountryKYCDate=new Date();
	}

	public String profileImagePath(){
		return this.profileImagePath;
	}

	public void profileImagePath(String profileImagePath){
		this.profileImagePath = profileImagePath;
	}

	public boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		this.password = KYCUtilities.bcryptEncryptor(password.trim());
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public Address getAddress() {
		return this.address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsKYCVerified() {
		return isKYCVerified;
	}
	public void setIsKYCVerified(boolean isKYCVerified) {
		this.isKYCVerified = isKYCVerified;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public List<KYCUserDocument> getKycUserDocumentArray() {
		return kycUserDocumentArray;
	}
	public void setKycUserDocumentArray(List<KYCUserDocument> kycUserDocumentArray) {
		this.kycUserDocumentArray = kycUserDocumentArray;
	}
	public List<LoginHistory> getLoginHistoryArray() {
		return loginHistoryArray;
	}
	public void setLoginHistoryArray(List<LoginHistory> loginHistoryArray) {
		this.loginHistoryArray = loginHistoryArray;
	}

	public String getSocialMeta() {
		return socialMeta;
	}

	public void setSocialMeta(String socialMeta) {
		this.socialMeta = socialMeta;
	}

	@ObjectHash
	public String rolename(){
		return this.role.getRoleName();		
	}

	@ObjectHash
	public List<Object> wallets(){
		if(!wallets.isEmpty()){
			return ObjectMap.objectMap(wallets);
		}
		return null;
	}

}
