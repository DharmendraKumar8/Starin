package com.starin.domain.documents.form;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.starin.domain.generalfield.UUIDField;




@Entity
@Table(name="Formfields",uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class FormField extends UUIDField{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer fid;
	

	private String name;
	private String label;
	private String value;

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FormField(){super(UUID.randomUUID().toString());}
	
	public FormField(String name,String label,String value){
	   super(UUID.randomUUID().toString());
	   this.name=name;
	   this.label=label;
	   this.value=value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

}
