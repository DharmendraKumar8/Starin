package com.starin.service;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.starin.conf.EnvConfiguration;
import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.User;



@Service

public class MailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired 
	EnvConfiguration configuration;
	
	@Autowired 
    private TemplateEngine templateEngine;

	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	public MailService(){}

	public static int noOfQuickServiceThreads = 20;
	/**
	 * this statement create a thread pool of twenty threads
	 * here we are assigning sned mail task using ScheduledExecutorService.submit();
	 */
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).
	
	/*
	 * Synchronously Sending mail 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void send(String email,String discription,String data) throws MailException,RuntimeException{
		logger.debug("sending synchronous mail");
		try{
		SimpleMailMessage mail=new SimpleMailMessage();
		mail.setFrom(configuration.getSender());
		mail.setTo(email);
		mail.setSubject(discription);
		mail.setText("Please click on the link "+data);
		sender.send(mail);
		}catch(Exception e){
          logger.error(e.getMessage(),e);
		}
	}
	/*
	 * Asynchronously Sending mail 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendScheduledMail(User user,String discription,String data) throws MailException,RuntimeException{
		logger.debug("sendScheduledMail ");
		SimpleMailMessage mail=new SimpleMailMessage();
		mail.setFrom(configuration.getSender());
		mail.setTo(user.getEmail());
		mail.setSubject(discription);
		mail.setText("Please click on the link "+data);
		quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
				sender.send(mail);
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
			}
		});
	}
	
	/*Asynchronously Sending mail 	
	*/
	@Transactional(propagation=Propagation.REQUIRES_NEW)	
	public void sendScheduledMail(RegisterUser registerUser,String discription,String data) throws MailException,RuntimeException
	{		
		SimpleMailMessage mail=new SimpleMailMessage();	
		mail.setFrom(configuration.getSender());		
		mail.setTo(registerUser.getEmail());		
		mail.setSubject(discription);		
		mail.setText("Please click on the link "+data);	
		quickService.submit(new Runnable() {	
			@Override			
			public void run() {	
				try{
					sender.send(mail);
					}catch(Exception e){
						logger.error(e.getMessage(),e);
					}	
				}		
			});	
		
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)	
	public void sendScheduleHtmlMail(RegisterUser user,String subject,Context context,String templatename){
		 try {
				String html=templateEngine.process(templatename, context);
	            MimeMessage mail = sender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
	    		mail.setFrom(configuration.getSender());
	            helper.setTo(user.getEmail());
	            helper.setSubject(subject);
	            helper.setText(html, true);
	            sender.send(mail);
	            logger.info("Send email to: "+user.getEmail());
	        } catch (Exception e) {
	            logger.error(String.format("Problem with sending email to: {}, error message: {}", user.getEmail(), e.getMessage()));
	            logger.error("email send error : ",e);
	         
	        }
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)	
	public void sendShedule(User user,String discription,String data) throws MailException,RuntimeException
	{		
		SimpleMailMessage mail=new SimpleMailMessage();	
		mail.setFrom(configuration.getSender());		
		mail.setTo(user.getEmail());		
		mail.setSubject(discription);		
		mail.setText(data);	
		quickService.submit(new Runnable() {	
			@Override			
			public void run() {	
				try{
					sender.send(mail);
					}catch(Exception e){
						logger.error(e.getMessage(),e);
					}	
				}		
			});	
		
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)	
	public void sendScheduleHtmlMail(User user,String subject,Context context,String templatename){
		 try {
				String html=templateEngine.process(templatename, context);
	            MimeMessage mail = sender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
	    		mail.setFrom(configuration.getSender());
	            helper.setTo(user.getEmail());
	            helper.setSubject(subject);
	            helper.setText(html, true);
	            sender.send(mail);
	            logger.info("Send email '{}' to: {}", subject,user.getEmail());
	            
	        } catch (Exception e) {
	            logger.error(String.format("Problem with sending email to: {}, error message: {}", user.getEmail(), e.getMessage()));
	         
	        }
	}

}
