package com.starin.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Custom Token Exception
   */

public class TokenAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;
	
	public TokenAuthenticationException(String msg) {
		super(msg);
	}

}
