package com.starin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * KYC security Configuration 
  */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
     
	/**
	 * Configuration of Public Urls
	 */
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		logger.debug("configuring public api access");
		web.ignoring().antMatchers("/api/v1/signup");
	
		web.ignoring().antMatchers("/api/v1/login");
														
		web.ignoring().antMatchers("/api/v1/users/email/verify**");
		web.ignoring().antMatchers("/api/v1/logout");
		web.ignoring().antMatchers("/api/v1/users/email/reset");
		web.ignoring().antMatchers("/api/v1/users/reset/password**");
		web.ignoring().antMatchers("/v2/api-docs/**");
		web.ignoring().antMatchers("/swagger-html");
		web.ignoring().antMatchers("/api/v1/users/wallets/search**");
		web.ignoring().antMatchers("/api/v1/users/wallet");
		web.ignoring().antMatchers("/api/v1/accounts/status");
		web.ignoring().antMatchers("/api/v1/countries");
	}	
	
    /*
     * Security Configuration for KYC Service. 
     * Registering CustomFilter,EntryPoint and authentication Manager
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		logger.debug("disabling session managment and specifying private apis");
		http.csrf().disable().
		sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authorizeRequests().antMatchers("/api/v1/**").authenticated();
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.addFilterBefore(new AuthenticationFilter(authenticationManager(),this.authenticationEntryPoint), BasicAuthenticationFilter.class);

	}
   
	@Bean
	public Jackson2JsonObjectMapper jackson2JsonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return new Jackson2JsonObjectMapper(mapper);
	}

	/*
	 * Creating bean for Authentication Manager
	 * Used by Spring Security
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		logger.debug("creating authentication Manager");
		return super.authenticationManagerBean();
	}
	/*
	 * Custom Authentication Provider for  PreAuthenticatedAuthenticationToken.class
	 */
	
	@Bean
	public AuthenticationProvider tokenAuthenticationProvider(){
		logger.debug("registering token authentication provider");
		return new TokenAuthProvider();
	}
	/*
	 * Registering Authentication Provider with Authentication
	 * Manager.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("registerinig token authentication provider with authentication manager");
		auth.authenticationProvider(tokenAuthenticationProvider());
	}

}