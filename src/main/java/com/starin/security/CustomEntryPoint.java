package com.starin.security;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *  Custom EntryPoint Implementations  for spring Security Filter Chain
 */

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint, Serializable {

		
	private static final Logger logger = LoggerFactory.getLogger(CustomEntryPoint.class);

	private static final long serialVersionUID = 1L;

	@Autowired
	private Jackson2JsonObjectMapper jackson2JsonObjectMapper;
    
	/*
     * Commence Method will be Called whenever some Authentication Exception 
     * Generating the Response.
     * 
     * @see org.springframework.security.web.AuthenticationEtntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
        logger.debug("commencing auth_exception");
		logger.debug("logging authenticatio message :->"+authException.getMessage());
		Map<String, Object> responseMessage = new HashMap<String, Object>();

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		responseMessage.put("timestamp",new Date());
		responseMessage.put("data", null);
		responseMessage.put("isSuccess",false);
		responseMessage.put("message",authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		try {
			String json = jackson2JsonObjectMapper.toJson(responseMessage);
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error("exception ",e);
		}
	}

}
