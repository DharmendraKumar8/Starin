package com.starin.domain.user;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginCredential {

	/*
	 * Login Credentials Wrapper Created for Swagger Documentation and is used By
	 * Spring for Validations
	 */

	@NotNull(message = "email can not be null")
	@NotEmpty(message = "email can not be Blank")
	private String email;

	@NotNull(message = "password can not be null")
	@NotEmpty(message = "password can not be Blank")
	private String password;

	/*
	 * @Pattern(regexp="(true|false)",message="forceLogin is of boolean type")
	 * private String forceLogin;
	 */

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

	/*
	 * public String getForceLogin() { return forceLogin; }
	 * 
	 * public void setForceLogin(String forceLogin) { this.forceLogin = forceLogin;
	 * }
	 */

}
