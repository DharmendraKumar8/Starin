package com.starin.exceptions;

public class CountryNotFoundException extends RuntimeException{

	/**
	 * defaut serial id
	 */
	private static final long serialVersionUID = 1L;

	public CountryNotFoundException(String message){
		super(message);
	}

}
