package com.starin.exceptions;

public class VerificationTokenNotVerifiedException extends RuntimeException {

	/**
	 * defaut serial id
	 */
	private static final long serialVersionUID = 1L;

	public VerificationTokenNotVerifiedException(String message) {
		super(message);
	}

}
