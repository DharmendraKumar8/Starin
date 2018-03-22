package com.starin.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.domain.documents.Country;
import com.starin.domain.graph.CountriesKYCDataWrapper;
import com.starin.domain.user.User;
import com.starin.domain.user.UserCountry;
import com.starin.repository.user.UserCountryRepository;
import com.starin.service.CountryService;
import com.starin.service.token.AuthTokenService;


@Service
public class UserCountryService {

	@Autowired
	private UserCountryRepository userCountryRepository;

	@Autowired
	private AuthTokenService authtokenservice;

	@Autowired
	private CountryService countryservice;

	private static final Logger logger = LoggerFactory.getLogger(UserCountryService.class);

	public UserCountryService() {}

	public UserCountry save(UserCountry userCountry) throws Exception{
		return userCountryRepository.save(userCountry);
	}

	public UserCountry findByUseridAndCountryid(Integer countryid,Integer userId){
		try{
			return userCountryRepository.findByUseridAndCountryid(countryid, userId);	
		}catch(Exception e){
			logger.error("Exception occur while fetch UserCountry obj by UserId and CountryId : ", e);
			return null;
		}
	}

	
	public UserCountry findByCountryAndUser(Country country,User user){
		try{
			return userCountryRepository.findByCountryAndUser(country, user);	
		}catch(Exception e){
			logger.error("Exception occur while fetch UserCountry obj by User and Country : ", e);
			return null;
		}
	}


	public List<Country> getUserCountries(User user){
		logger.info("Inside getUserCountries method of UserCountryService");
		try{
			return userCountryRepository.getUserCountries(user);
		}catch (Exception e) {
			logger.error("Exception occur while fetch all country of user from UserCountry : ", e);
			return null;
		}

	}
	public List<Map<String,Object>> getUserCountriesWithKYC(String authtoken){
		User user =authtokenservice.findByToken(authtoken).getUser();
		logger.info("Inside getUserCountriesWithKYC method of UserCountryService");
		List<UserCountry> usercountries=userCountryRepository.findByUser(user);
		List<Map<String,Object>> usercountriesMap=new LinkedList<Map<String,Object>>();
		for(UserCountry usercountry:usercountries){
			Map<String,Object> res=new HashMap<String,Object>();
			res.put("country",countryservice.getModifiedCountryIdKey(usercountry.getCountry()));
			res.put("kycstatus", usercountry.isKycVerificationStatus());
			usercountriesMap.add(res);
		}
		usercountries=null;
		return usercountriesMap;
	}

	public List<UserCountry> findByUser(User user){
		return userCountryRepository.findByUser(user);
	}
	
	public List<CountriesKYCDataWrapper> findCountryKYCGraphBtwDates(String startDate,String endDate){
		List<Object[]> data=userCountryRepository.findCountryKYCGraphBtwDates(startDate, endDate);
		return data.stream().map(result ->new CountriesKYCDataWrapper(result[0].toString(), result[3].toString(), result[2].toString(),Integer.parseInt(result[1].toString()))).collect(Collectors.toList());
	}

}

