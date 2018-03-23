package com.starin.domain.documents;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.starin.domain.generalfield.UUIDField;
import com.starin.domain.user.User;
import com.starin.utils.ObjectHash;



@Entity
public class KYCUserDocument extends UUIDField{
	
	@Id
	@GeneratedValue( strategy=GenerationType.AUTO )
    private Integer kycUserDocumentID;
    
	private String url;

	private Date uploadedAt;
	
	@Column(columnDefinition="json")
	private String documentmeta;
	
	public String getDocumentmeta() {
		return documentmeta;
	}

	public void setDocumentmeta(String documentmeta) {
		this.documentmeta = documentmeta;
	}

	public Integer getKycUserDocumentID() {
		return kycUserDocumentID;
	}

	public void setKycUserDocumentID(Integer kycUserDocumentID) {
		this.kycUserDocumentID = kycUserDocumentID;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Date VerifedAt;

	private String verifiedBy;

	private String documentStatus;

	@ManyToOne
	@JoinColumn(name="userID")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "kyc_doc_id", nullable = false)
	private KYCDoc kycDoc;
	
	public KYCUserDocument(){
		super(UUID.randomUUID().toString());
	}
	
	public KYCUserDocument(String url,Date uploadedAt,String documentStatus,KYCDoc kycDoc,User user){
		super(UUID.randomUUID().toString());
		this.url = url;
		this.uploadedAt = uploadedAt;
		this.documentStatus = documentStatus;
		this.kycDoc = kycDoc;
		this.user = user;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public Date getVerifedAt() {
		return VerifedAt;
	}

	public void setVerifedAt(Date verifedAt) {
		VerifedAt = verifedAt;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public KYCDoc getKycDoc() {
		return kycDoc;
	}

	public void setKycDoc(KYCDoc kycDoc) {
		this.kycDoc = kycDoc;
	}

	@ObjectHash
	public String uploadedAt(){
		return this.uploadedAt+"Z";
		//return KYCDateUtil.convertDateInToUTCTimeZone(this.uploadedAt);
	}

}
