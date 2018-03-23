package com.starin.domain.generalfield;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/*
 * Class UUIDField use for add uuid common fields in entities where need
 */
@MappedSuperclass
public abstract class UUIDField implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="uuid",unique=true,length=40)
	@NotNull
	private String uuid;

	public UUIDField(String uuid){
		this.uuid=uuid;
	}
	
	public String getUuid() {
		return uuid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	

}