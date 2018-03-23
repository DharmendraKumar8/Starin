package com.starin;

import java.util.TimeZone;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.starin.conf.EnvConfiguration;
import com.starin.security.SimpleCORSFilter;


@SpringBootApplication
public class StarinApplication {

	@Autowired
	private EnvConfiguration configuration;

	private static final Logger logger = LoggerFactory.getLogger(StarinApplication.class);

	public static void main(String[] args) {
		logger.debug("starting Starin application");
		SpringApplication.run(StarinApplication.class, args);
	}
	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone(configuration.getServerTimeZone()));
	}
	@Bean
	public Executor asyncExecutor() {
		logger.debug("Creating Async Executor Service");
		ThreadPoolTaskExecutor executorpool = new ThreadPoolTaskExecutor();
		executorpool.setCorePoolSize(10);
		executorpool.setMaxPoolSize(50);
		executorpool.setQueueCapacity(100);
		executorpool.setThreadNamePrefix("Starin_workers");
		executorpool.initialize();
		return executorpool;
	}
	
	/**
	 * This Bean is used to register Global CORS filter 
	 * in Spring Context. Order of this filter is highest
	 * in Starin.
	 */
	@Bean
	public Filter corsfilter(){
		logger.debug("Creating global SimpleCORSFilter");
		return new SimpleCORSFilter();
	}
}
