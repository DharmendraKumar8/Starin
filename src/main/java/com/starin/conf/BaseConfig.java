package com.starin.conf;

import java.util.Date;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starin.domain.documents.Country;
import com.starin.domain.role.Role;
import com.starin.domain.role.SuperUserRole;
import com.starin.domain.user.Address;
import com.starin.domain.user.HUserDetail;
import com.starin.domain.user.User;
import com.starin.domain.user.UserCountry;
import com.starin.domain.user.UserCountryId;
import com.starin.exceptions.UserCountryMappingException;
import com.starin.repository.ActivityRepository;
import com.starin.repository.CountryRepository;
import com.starin.repository.RoleRepository;
import com.starin.repository.user.UserCountryRepository;
import com.starin.repository.user.UserRepository;
import com.starin.service.token.TokenService;
import com.starin.service.token.TokenServiceImpl;
import com.starin.utils.KYCUtilities;
import com.starin.repository.user.HUserDetailRepository;

@ComponentScan(basePackages={ "com.starin","com.starin.controller","com.starin.security","com.starin.domain","com.starin.repository" })
@PropertySource({ "classpath:application.properties" })
@Configuration
public class BaseConfig {

	@Autowired 
	private  EnvConfiguration configuration;

	@Autowired 
	RoleRepository roleRepository;

	@Autowired 
	ActivityRepository activityRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	private  SuperUserRole superUserRole; 

	@Autowired
	private UserCountryRepository userCountryRepository;

	@Autowired
	private HUserDetailRepository hUserDetailRepository;

	public  static HashMap<String,Integer> roles_level;


	private static final Logger logger = LoggerFactory.getLogger(BaseConfig.class);


	/**
	 * Creating a default Admin user at startup
	 * reading Admin configuration from application.properties file
	 * @return  adminUser
	 */

	public User createDefaultAdmin(){
		logger.debug("Default Admin Creation In Progress");
		Role adminRole = null;
		User adminUser = null;
		Country country = null;
		try{
			adminUser = userRepository.findByEmail(configuration.getAdminEmailId());
			if(adminUser != null){
				logger.debug("Admin User already created................");
				
				return adminUser;
			}
			adminRole = roleRepository.findByRoleName(configuration.getAdminRoleName());
			if(adminRole == null){
				logger.debug("Role Name : \""+configuration.getAdminRoleName()+"\"  not exist");
				return null;
			}
			country = countryRepository.findByCountryName(configuration.getAdminCountry());
			if(country == null){
				logger.debug("Country Name : \""+configuration.getAdminCountry()+"\"  not exist");
				return null;
			}
			Address address = new Address(country.getCountryName());
			Short defaultCountryId = Short.parseShort(country.getCountryID()+"");
			adminUser = new User(configuration.getAdminRoleName(),KYCUtilities.bcryptEncryptor(configuration.getAdminPassword()),configuration.getAdminEmailId(),adminRole,new Date(),address,defaultCountryId);
			adminUser.setIsActive(true);
			adminUser.setIsEmailVerified(true);
			adminUser =	userRepository.save(adminUser);
			try{
				UserCountry userCountry = new UserCountry();
				userCountry.setUser(adminUser);
				userCountry.setCountry(country);
				userCountry.setUserCountryId(new UserCountryId(adminUser.getUid(), country.getCountryID()));
				userCountry.setKycDate(new Date());
				userCountryRepository.save(userCountry);
			}catch(Exception e){
				logger.error("Exception at saving UserCountry Mapping", e);
				throw new UserCountryMappingException("UserCountry not save in Database");
			}
			try{
				hUserDetailRepository.save(new HUserDetail(adminUser.getUid()) );
			}catch (Exception e) {
				logger.error("Exception at saving HUserDetail ", e);
			}
		}catch(Exception e){
			logger.error("Admin User Not Created"+e);
		}
		return adminUser;
	}

	public User superUser(){
		logger.debug("Super User Creation in progress");
		Role superRole = null;
		User superUser = null;
		Country country = null;
		try{
			superUser = userRepository.findByEmail(configuration.getSuperUserEmail());
			if(superUser != null){
				logger.debug("Super Admin User already created................");
				return superUser;
			}
			superRole = roleRepository.findByRoleName(superUserRole.getRolename().trim());
			if(superRole == null){
				logger.debug("Role Name : \""+superUserRole.getRolename().trim()+"\"  not exist");
				return null;
			}
			country = countryRepository.findByCountryName(configuration.getSuperUserCountry());
			if(country == null){
				logger.debug("Country Name : \""+configuration.getSuperUserCountry()+"\"  not exist");
				return null;
			}
			Address address = new Address(country.getCountryName());
			Short defaultCountryId = Short.parseShort(country.getCountryID()+"");
			superUser = new User(superUserRole.getRolename(),KYCUtilities.bcryptEncryptor(configuration.getSuperUserPassword()),configuration.getSuperUserEmail(),superRole,new Date(),address,defaultCountryId);
			superUser.setIsActive(true);
			superUser.setIsEmailVerified(true);
			superUser.setIsKYCVerified(true);
			superUser = userRepository.save(superUser);
			try{
				UserCountry userCountry = new UserCountry();
				userCountry.setUser(superUser);
				userCountry.setCountry(country);
				userCountry.setUserCountryId(new UserCountryId(superUser.getUid(), country.getCountryID()));
				userCountry.setKycVerificationStatus(true);
				userCountry.setKycDate(new Date());
				userCountryRepository.save(userCountry);
			}catch(Exception e){
				logger.error("Exception at saving UserCountry Mapping", e);
				throw new UserCountryMappingException("UserCountry not save in Database");
			}
		}catch(Exception e){
			logger.error("Super Admin User Not Created"+e);
		}
		return superUser;
	}

	@Bean(name="superuser")
	User superUserCreation(){
		User superuser=this.superUser();
		this.createDefaultAdmin();
		if(roles_level==null){
			roles_level=new HashMap<String,Integer>();
			roles_level.put(superUserRole.getRolename().toLowerCase().trim(), 1);
			roles_level.put(configuration.getAdminRoleName().toLowerCase().trim(), 2);
		}
		return superuser;
	}
	/**
      Creating a bean of MessageSource for reading properties file
	 * @return MessageSource bean
	 */
	@Bean
	MessageSource messageSource(){
		logger.debug("MessageSource Bean creation...");
		ReloadableResourceBundleMessageSource messageSource =  new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:locale/messages", "classpath:locale/errors");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(3600);
		return messageSource;
	}

	/**
	 * Service bean for generating token String
	 * this is used by various Token Domain for
	 * generating UUID
	 * @return  tokenservice bean
	 */

	@Bean
	TokenService tokenservice(){
		logger.debug("TokenService Bean creation...");
		return new TokenServiceImpl();
	}
	/**
	 * Json Object mapper used by Spring boot
	 * @return
	 */

	@Bean
	public Jackson2JsonObjectMapper jackson() {
		logger.debug("Jackson2JsonObjectMapper Bean creation...");
		ObjectMapper mapper = new ObjectMapper();
		return new Jackson2JsonObjectMapper(mapper);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}	

}

