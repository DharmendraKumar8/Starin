package com.starin.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.starin.exceptions.KYCException;
import com.starin.exceptions.TokenAuthenticationException;
import com.starin.utils.ResponseUtil;

/**
 * Custom Security Filter  Placed
 * First in Spring Security Filter
 * Chain.
 * 
 */


public class AuthenticationFilter extends GenericFilterBean{

	private AuthenticationManager authenticationManager;
	private CustomEntryPoint entrypoint;
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	public AuthenticationFilter(AuthenticationManager authenticationManager,AuthenticationEntryPoint entry){
		this.authenticationManager=authenticationManager;
		this.entrypoint=(CustomEntryPoint)entry;
	}
	/**
	 * Custom Filter for processing authentication
	 * of user in security filter chain.
	 */

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterchain)
			throws IOException, ServletException{
        logger.debug("Enabling cors in security");
        HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request=(HttpServletRequest) req;    
		
		logger.debug("Inside my authentication filter Authentication Check");
		HttpServletRequest hrequest=(HttpServletRequest)request;
		HttpServletResponse hresponse=(HttpServletResponse)res;
		String token=hrequest.getHeader("belrium-token");
		logger.debug("passed token in header :"+token);
		if(token==null){
			logger.debug("Token is empty");
		}		
		else if(!token.isEmpty() ){
			logger.debug("Processing authentication");
			try{
			processTokenVerification(token);
			}catch(KYCException exception){
				ResponseUtil.genrate(hresponse, exception.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}
		logger.debug("Outside processing authentication");
		filterchain.doFilter(request, response);
	}
	
     /*
      *  Requesting the token Authentication
      *  And placing the result in Security Context 
      */
	public void processTokenVerification(String token) throws KYCException{

		logger.debug("Inside process token verification");
		Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
		logger.debug("Get a handle on result of authentication");
		if(resultOfAuthentication.isAuthenticated()){
			SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
			logger.debug("Setting the result in security context");
		}
		else{
			logger.debug("failed to Authenticate");
		}
	}
	/*
	 * Creating PreAuthenticatedAuthenticationToken and setting details
	 * and passing it to authentication Manager
	 */
	private Authentication tryToAuthenticateWithToken(String token) throws KYCException{
		PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
				null);
		requestAuthentication.setDetails(token);

		logger.debug("token value :"+requestAuthentication.getDetails().toString());
		try{
			logger.debug("token value :"+requestAuthentication.getName());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		logger.debug("returning response authentication to processTokenVerification");
		return tryToAuthenticate(requestAuthentication);
	}
	
	/*
	 * Authentication Manager Authenticating Token Through
	 * Registered Authentication Provider
	 */
	
	private Authentication tryToAuthenticate(Authentication requestAuthentication) throws KYCException {
		Authentication responseAuthentication = null;

		try {
			logger.debug("tryToAuthenticate :");

			logger.debug("Authentication manager trying to authenticate with registered authentication provider");
			responseAuthentication = authenticationManager.authenticate(requestAuthentication);
			
		} catch (InvalidDataAccessApiUsageException e) {
			logger.error("invalid api usage",e);
			throw new InvalidDataAccessApiUsageException("Invalid token");
		}
		catch (TokenAuthenticationException e) {
			logger.debug("failed to authenticate",e);
			throw new TokenAuthenticationException("Fail to asddsadsadsadsadsa");
		}
		logger.debug("returning authentication to tryToAuthenticateWithToken");
		return responseAuthentication;
	}
}
