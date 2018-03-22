package com.starin.service.token;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.starin.conf.EnvConfiguration;
import com.starin.constants.KYCConstants;
import com.starin.domain.VerificationToken;
import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.User;
import com.starin.repository.token.VerificationTokenRepository;
import com.starin.service.MailService;


@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository vtokenrepo;
    
	@Autowired
	private TokenService tokenservice;
	
	@Autowired
	private MailService mailservice;
	
	@Autowired 
	private EnvConfiguration configuration;
	
	private static final Logger logger = LoggerFactory.getLogger(VerificationTokenService.class);
	
	@Transactional
	public VerificationToken findByToken(String token){
		return vtokenrepo.findByToken(token);
	}
	@Transactional
	public VerificationToken findByUser(User user){
		return vtokenrepo.findByUser(user);
	}
	
	@Transactional
	public VerificationToken findByRegisterUser(RegisterUser user){
		return vtokenrepo.findByRegisterUser(user);
	}
	@Transactional
	public VerificationToken save(VerificationToken token){
		return vtokenrepo.save(token);
	}
	
	/**
	 * This method is used to update verification token when expired
	 * @param token
	 */
	public void onExpire(VerificationToken token){
		logger.debug("verification token updation");
		token.setCreationDate(new Date());
		String newtoken=tokenservice.genrate();
		token.setToken(newtoken);
		logger.debug("updated verfication token as new");
		save(token);
		RegisterUser register_user=token.getRegisterUser();
		try{
			Context context = new Context();
		 	context.setVariable("username", register_user.getName());
		 	context.setVariable("verificationLink", configuration.getFrontEndUrl()+"?token="+token.getToken());
			logger.debug("sync mail send in progress");
		 	mailservice.sendScheduleHtmlMail(register_user,"Verification link", context, KYCConstants.verificationTemplateName);
		
			//mailservice.send(token.getRegisterUser().getEmail(), "Verification link",configuration.getFrontEndUrl()+"?token="+newtoken);
			logger.debug("mail send done");
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}
	
}
