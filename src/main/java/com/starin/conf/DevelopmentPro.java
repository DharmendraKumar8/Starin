package com.starin.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.starin.utils.MessageUtility;


@Configuration
@Profile("dev")
public class DevelopmentPro {

	private static final Logger logger = LoggerFactory.getLogger(DevelopmentPro.class);
	
	/**
	 * Bean for Reading developement configuation from 
	 * application.properties file
	 * @return EnvConfiguration bean
	 	 */
	@Bean
	public EnvConfiguration getDevelopmentConfig(){
		logger.debug("EnvConfiguration Bean creation");
		return new DevelopmentEnv();
	}
	/**
	 * Message Utility for fetching successMessages and
	 * errorMessages  
	 * @return MessageUtility
		 */
	@Bean
	public MessageUtility getMessageUtility(){
		logger.debug("MessageUtility Bean creation");
		MessageUtility messageUtility = new MessageUtility();
		messageUtility.initialze();
		return messageUtility;
	}
	
}
