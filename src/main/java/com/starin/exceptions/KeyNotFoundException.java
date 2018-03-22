package com.starin.exceptions;


public class KeyNotFoundException extends RuntimeException {
	
	/**
	 * defaut serial id
	 */
	private static final long serialVersionUID = 1L;
	
	public KeyNotFoundException(String message) {
		super(message);
    }
	
}
