package com.starin.domain.user;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Password {

	/*
	 * Password Credentials Wrapper Created for Swagger Documentation and is used By
	 * Spring for Validations
	 */

	@NotNull(message = "password can not be null")
	@Column(name = "password")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password atleast one uppercase, one lowercase , one number and one special charcter")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
