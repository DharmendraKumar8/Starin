package com.starin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.domain.documents.Country;
import com.starin.repository.CountryRepository;
import com.starin.utils.ObjectMap;

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

	public CountryService() {}
	
	public List<String> findAllCountryName(){
		return countryRepository.findAllCountryName();
	} 

	/**
	 * Method called after CountryService bean field initialization
	 * For inserting available countries from country csv to kyc database
	 */
	/*
	 * 
	 * @Autowired
	   private ResourceLoader resourceLoader;

	@PostConstruct
	public void init() {
		Resource resource = resourceLoader.getResource("classpath:"+KYCConstants.countryPath);
		LOGGER.debug("loading countries from country.csv files");
		List<Country> countryArray = null;
		try {
			countryArray = KYCUtilities.loadCSVFile(Country.class,resource.getInputStream());
		} catch (Exception e) {
			LOGGER.error("Exception occur while load county.csv file : ", e);
			e.printStackTrace();
			return;
		}
		List<Country> dbCountryArray = getAllCountry();
		if(dbCountryArray != null){
			List<Country> newCountryArray = KYCUtilities.getListA_Minus_ListBObjects(countryArray,dbCountryArray, "countryName");
			LOGGER.debug("saving countries in database"+dbCountryArray.size());
			for(Country country : newCountryArray){
			     saveCountry(country);
			}
		}
	}*/

	public Country saveCountry(Country country){
		try{
			return country = this.countryRepository.save(country);
		}catch (Exception e) {
			LOGGER.error("Exception at Country saving", e.getMessage());
			return null;
		} 
	}

	public void saveCountry(List<Country> countryList){
		try{
			this.countryRepository.save(countryList);
		}catch (Exception e) {
			LOGGER.error("Exception on Country insertion in Database : ",e.getMessage());
		}
	}

	public List<Country> getAllCountry(){
		try{
			return countryRepository.findAll();
		}catch(Exception e){
			LOGGER.error("Exception occur while fething all Countries from database ",e.getMessage());	
			return null;
		}
	}

	public Country findByCountryName(String countryName){
		try{
			return countryRepository.findByCountryName(countryName);
		}catch(Exception e){
			LOGGER.error("Exception at Country fetching by Name : ",e.getMessage());	
			return null;
		}
	}

	public Country findByCountryID(Integer countryID){
		try{
			return countryRepository.findByCountryID(countryID);
		}catch(Exception e){
			LOGGER.error("Exception at Coutry fetching by countryID : ",e.getMessage());	

		}
		return null;
	}
	
	public Map<String,Object> getModifiedCountryIdKey(Country country){
		Map<String,Object> response = new HashMap<String,Object>();
		response = ObjectMap.objectMap(country);
		response.remove("countryID");
		response.put("countryId", country.getCountryID());
		return response;
	}

}
