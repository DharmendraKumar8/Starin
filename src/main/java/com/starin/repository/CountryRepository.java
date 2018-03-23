package com.starin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.starin.domain.documents.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

	public Country findByCountryName(String countryName);
	public Country findByCountryID(Integer countryID);
	
	
	@Query(value="select country_name from country order by country_name",nativeQuery=true)
	public List<String> findAllCountryName();
	
}
