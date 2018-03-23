package com.starin.domain.documents;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.starin.domain.documents.form.KYCDocFormMeta;
import com.starin.domain.generalfield.UUIDField;


@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"kycDocumentMetaId", "kycDocumentTypeId","countryId"}))
public class KYCDoc extends UUIDField{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer kycDocId; 
	
	//@Id
	private Integer kycDocumentMetaId;
	
	//@Id
    private Integer kycDocumentTypeId;
	
	//@Id
	private Integer countryId;
	
	private boolean isMandatory;
	
	
	
	@OneToMany(mappedBy="doc")
    private Set<KYCDocFormMeta> properties=new HashSet<KYCDocFormMeta>();
	
	public Set<KYCDocFormMeta> getProperties() {
		return properties;
	}

	public void setProperties(Set<KYCDocFormMeta> properties) {
		this.properties = properties;
	}

	public KYCDoc(){
		super(UUID.randomUUID().toString());
	}
	
	public KYCDoc(Integer countryId,Integer kycDocumentTypeId,Integer kycDocumentMetaId,boolean isMandatory){
		super(UUID.randomUUID().toString());
		this.kycDocumentTypeId=kycDocumentTypeId;
		this.kycDocumentMetaId=kycDocumentMetaId;
		this.countryId=countryId;
		this.isMandatory = isMandatory;
	}
	
	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Integer getKycDocumentMetaId() {
		return kycDocumentMetaId;
	}

	public void setKycDocumentMetaId(Integer kycDocumentMetaId) {
		this.kycDocumentMetaId = kycDocumentMetaId;
	}

	public Integer getKycDocumentTypeId() {
		return kycDocumentTypeId;
	}

	public void setKycDocumentTypeId(Integer kycDocumentTypeId) {
		this.kycDocumentTypeId = kycDocumentTypeId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
		
	public Integer getKycDocId() {
		return kycDocId;
	}

	public void setKycDocId(Integer kycDocId) {
		this.kycDocId = kycDocId;
	}
	
}
