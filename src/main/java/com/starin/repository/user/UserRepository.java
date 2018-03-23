package com.starin.repository.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.starin.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	User findByEmail(String email);

	User findByUid(Integer id);

	@Query(value = "select COUNT(roleid)as total,COUNT(case when user.roleid=:adminid then 1 end) as admin,COUNT(case when user.roleid=:userid then 1 end) as users,COUNT(case when user.roleid=:merchantid then 1 end) as merchant,COUNT(case when user.is_active=1 then 1 end) as active_user from user", nativeQuery = true)
	String getUserMeta(@Param("adminid") long adminid, @Param("merchantid") long merchantid,
			@Param("userid") long userid);

	@Query(value = "select uid,name,email,isKYCVerified from User u where u.isKYCVerified=:verify and u.role.roleName!=:rolename")
	List<Object[]> findByisKYCVerified(@Param("verify") boolean verify, @Param("rolename") String rolename,
			Pageable page);

	@Query(value = "select uid,name,email,isKYCVerified from User u where u.role.roleName!=:rolename")
	List<Object[]> findByisKYCVerified(@Param("rolename") String rolename, Pageable page);

	@Query(value = "select count(*) as total from user where date(default_country_kycdate) between :startDate and :endDate and iskyc_verified=:kycstatus", nativeQuery = true)
	Integer countByCountryKycBetweenPassedDate(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("kycstatus") boolean kycstatus);

	@Query(value = "select year,ifnull(verified, 0) as kycverified,ifnull(unverified, 0) as kycunverifed from (select year(default_country_kycdate) as year from user where year(default_country_kycdate) between :startYear and :endYear group by year(default_country_kycdate) ) as tbl1 left join ( select count(*) as verified, year(default_country_kycdate) as year2 from user where iskyc_verified = true and year(default_country_kycdate) between :startYear and :endYear group by year(default_country_kycdate)) tbl2 on year = year2 left join ( select count(*) as unverified,year(default_country_kycdate) as year3 from user where iskyc_verified = false and year(default_country_kycdate) between :startYear and :endYear group by year(default_country_kycdate)) tbl3 on year = year3;", nativeQuery = true)
	// @Query(value="select year(default_country_kycdate) as year,count(*) as total
	// from user where year(default_country_kycdate) between :startYear and :endYear
	// and iskyc_verified=:isKycVerified group by year(default_country_kycdate)
	// order by year",nativeQuery=true)
	List<Object[]> usersYearlyKYCStatusGraphData(@Param("startYear") Integer startYear,
			@Param("endYear") Integer endYear);

	@Query(value = "select country_name,countryid,ifnull(total,0) as total from country left join (select default_country_id,count(*) as total from user where date(register_date) between :startDate and :endDate group by(default_country_id)) t2 on countryid=default_country_id order by country_name;", nativeQuery = true)
	List<Object[]> getUserCountPercentageByCountry(@Param("startDate") String startDate,
			@Param("endDate") String endDate);

}
