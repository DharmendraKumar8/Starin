package com.starin.domain.user;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Email {
	/*
	 * Email Wrapper Created for Swagger Documentation and is used By Spring for
	 * Validations
	 */

	@Column(name = "email", unique = true)
	@NotNull(message = "email can not be null")
	@Size(min = 1, message = "email can not be empty")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
