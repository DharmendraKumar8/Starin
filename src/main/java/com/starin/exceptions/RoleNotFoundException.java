package com.starin.exceptions;

public class RoleNotFoundException extends RuntimeException{
	
	/**
	 * defaut serial id
	 */
	private static final long serialVersionUID = 1L;

	public RoleNotFoundException(String message){
		super(message);
	}

}
