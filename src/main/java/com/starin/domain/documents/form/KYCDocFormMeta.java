package com.starin.domain.documents.form;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.starin.domain.documents.KYCDoc;
import com.starin.domain.generalfield.UUIDField;
import com.starin.utils.ObjectHash;

@Entity
@Table(name="KYCDocformMeta",uniqueConstraints=@UniqueConstraint(columnNames={"fid", "kycdocid"}))
public class KYCDocFormMeta extends UUIDField{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long fieldid;

	@ObjectHash
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="kycdocid")
	private KYCDoc doc;
	
	@ObjectHash
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="fid")
	private FormField field;

	private boolean required;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;


	@Column(columnDefinition="json",name="patternjson")
	private String pattern;
	
	private String maxlength;

	private String minlength;
	
	@Column(name="validationerrormessage")
	private String validationErrorMessage;
	
	private String placeholder;
	
	public KYCDocFormMeta(){super(UUID.randomUUID().toString());};

	public KYCDocFormMeta(KYCDoc doc,FormField field,boolean required,String patternjson,String placeholder,String maxlength,String minlength,String validationErrorMessage,String type){
		super(UUID.randomUUID().toString());
		this.doc=doc;
		this.field=field;
		this.minlength=minlength;
		this.maxlength=maxlength;
		this.pattern=patternjson;
		this.placeholder=placeholder;
		this.required=required;
		this.validationErrorMessage=validationErrorMessage;
		this.type=type;
	}
	
	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}
	
	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getMinlength() {
		return minlength;
	}

	public void setMinlength(String minlength) {
		this.minlength = minlength;
	}

	public KYCDoc getDoc() {
		return doc;
	}

	public void setDoc(KYCDoc doc) {
		this.doc = doc;
	}

	public FormField getField() {
		return field;
	}

	public void setField(FormField field) {
		this.field = field;
	}

	public Long getFieldid() {
		return fieldid;
	}

	public void setFieldid(Long fieldid) {
		this.fieldid = fieldid;
	}

	public boolean isRequired() {
		return required;
	}
	public boolean getRequired(){
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
