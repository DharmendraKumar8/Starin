package com.starin.domain.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_ProfileUpdate {

	@Valid
	private Req_Address address;

	@NotNull(message="Name can not be null")
	@NotEmpty(message = "Name can not be null")
	@Pattern(regexp="^(?=.{5,100}$)(?!.* {2,})[a-zA-Z][a-zA-Z ]*[a-zA-Z]$",message="name is not valid")
	@Size(min=5,max=100,message="Allowed length of username is minimum 5 and maximum 100 ")
	private String name;

	public Req_Address getAddress() {
		return address;
	}
	public void setAddress(Req_Address address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
