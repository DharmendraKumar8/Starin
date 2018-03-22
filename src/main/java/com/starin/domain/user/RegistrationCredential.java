package com.starin.domain.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;





public class RegistrationCredential {
	
	/*
	 * Registration Credentials Wrapper Created for  Swagger DocumentationRegistrationCredential
	 * and is used By Spring for Validations
	 */
	
	@NotNull(message="Email is mandatory")
	@Size(max=250,message="email can only be of 250 characters")
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message="Invalid email")
	@NotEmpty(message="Email can not be empty")
	private String email;

	@NotNull(message="password can not be null")	
	@Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",message="Password atleast one uppercase, one lowercase , one number and one special charcter")
	private String password;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotNull(message="Please specify user type")
	@NotEmpty(message="Please specify user type")
	private String type;

	@NotEmpty(message = "Name can not be null")
	@Pattern(regexp="^(?=.{5,100}$)(?!.* {2,})[a-zA-Z][a-zA-Z ]*[a-zA-Z]$",message="name is not valid")
	@Size(min=5,max=100,message="Allowed length of username is minimum 5 and maximum 100 ")
	@NotNull(message = "Name can not be null")
	private String name;

	@NotNull(message = "countryId can not be Null")
	@NotEmpty(message = "countryId can not be Empty")
	@Pattern(regexp="[0-9]+",message="CountryId should be an Integer")
	private String countryId;
	
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.password = password.trim();
	}
	
	public Integer parseCountryIdIntoInteger(){
		try{
			return Integer.parseInt(this.countryId);
		}catch(Exception e){
			return null;
		}
	}

}
