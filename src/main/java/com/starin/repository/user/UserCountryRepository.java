package com.starin.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.starin.domain.documents.Country;
import com.starin.domain.user.User;
import com.starin.domain.user.UserCountry;
import com.starin.domain.user.UserCountryId;


@Repository
public interface UserCountryRepository extends JpaRepository<UserCountry, UserCountryId>{

	@Query(value="From UserCountry uc where uc.user.uid=:userId and uc.country.countryID=:countryId")
	UserCountry findByUseridAndCountryid(@Param("countryId") Integer countryId,@Param("userId")Integer userId);

	@Query("select usrCountry.country from UserCountry usrCountry where usrCountry.user=:user")
    public List<Country> getUserCountries(@Param("user") User user);
	
	public List<UserCountry> findByUser(User user);

	/**
	 * calulate coutries based kyc status 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	

	@Query(value="select country_name,countryid,ifnull(non_verified,0) as non_verified,ifnull(verified,0) as verified from (select country_name,countryid,ifnull(non_verified,0) as non_verified from country left join (select count(country_id) as non_verified,country_id from user_country where  kyc_verification_status=false and date(kyc_date) between :startDate and :endDate group by(country_id)) t1 on country_id=countryid) t2 left join (select count(country_id) as verified,country_id from user_country where  kyc_verification_status=true and date(kyc_date) between :startDate and :endDate group by(country_id)) t3 on country_id=countryid order by country_name",nativeQuery=true)
	public List<Object[]> findCountryKYCGraphBtwDates(@Param("startDate")String startDate,@Param("endDate")String endDate);

	UserCountry findByCountryAndUser(Country country,User user);

}
