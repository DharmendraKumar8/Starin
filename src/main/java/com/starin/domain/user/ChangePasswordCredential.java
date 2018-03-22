package com.starin.domain.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangePasswordCredential {

	@NotEmpty(message = "Please enter old password")
	@Size(min = 8, max = 20, message = "Size should be between 8 and 20")
	@NotNull(message = "old password can not be empty")
	private String oldPassword;

	@NotEmpty(message = "Please enter new Password")
	@Size(min = 8, max = 20, message = "Size should be between 8 and 20")
	@NotNull(message = "New password can not be empty")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password atleast one uppercase, one lowercase , one number and one special charcter")
	private String newPassword;

	@NotEmpty(message = "Please enter confirm Password")
	@Size(min = 8, max = 20, message = "Size should be between 8 and 20")
	@NotNull(message = "confirm password can not be empty")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password atleast one uppercase, one lowercase , one number and one special charcter")
	private String confirmPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
