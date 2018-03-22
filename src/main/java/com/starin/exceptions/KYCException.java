package com.starin.exceptions;

public class KYCException extends RuntimeException {
	/**
	 * defaut serial id
	 */
	private static final long serialVersionUID = 1L;
	public KYCException(String message){
		super(message);
	}
	
}
