package com.starin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.starin.conf.EnvConfiguration;
import com.starin.domain.AuthenticationToken;
import com.starin.exceptions.KYCException;
import com.starin.repository.token.AuthTokenRepository;
import com.starin.utils.KYCDateUtil;
import com.starin.utils.MessageUtility;


/**
 * Custom Token Authentication Provider
 */

public class TokenAuthProvider implements AuthenticationProvider{

	@Autowired
	private AuthTokenRepository authrepository;
	@Autowired
	private EnvConfiguration configuration;
	
	
	private static final Logger logger = LoggerFactory.getLogger(TokenAuthProvider.class);
	
	@Override
	public Authentication authenticate(Authentication requestAuthentication) throws AuthenticationException {
		logger.debug("Inside my Authentication Provider");
		AuthenticationToken token=authrepository.findByToken(requestAuthentication.getName().trim());
		if(token!=null){
			if(KYCDateUtil.isLinkExpire(token.getCreationDate(), Integer.parseInt(configuration.getSessionExpirationTime()))){
				throw new KYCException(MessageUtility.getErrorMessage("SessionExpired"));
			}
			requestAuthentication.setAuthenticated(true);
			logger.debug("User successfully Authenticated"); 
			logger.debug(token.getUser().getEmail()+" authenticated successfully.");
			return requestAuthentication;
		}else{//user null
			requestAuthentication.setAuthenticated(false);
			logger.debug("User Authentication Fail");
			return requestAuthentication;			  
		}
	}

	/*
	 * This method specify that provider is able to support PreAuthenticationToken authentication(non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	
	@Override
	public boolean supports(Class<?> authentication) {
		logger.debug("registering it as PreAuthenticatedAuthenticationToken AuthProvider");
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}

}
