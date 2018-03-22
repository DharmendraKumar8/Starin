package com.starin.repository.user;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.starin.domain.user.RegisterUser;

public interface RegisterUserRepository extends JpaRepository<RegisterUser, Integer>{
	
	RegisterUser findByEmail(String email);
	
	RegisterUser findByRegisterUserId(Integer id);
	
	@Transactional
	@Modifying
	@Query("delete RegisterUser where (( ( ((day(:currentDate)-1) * 24)+ hour(:currentDate) )*60 + minute(:currentDate) )  - ((((day(createdAt)-1) * 24)+ hour(createdAt))*60 +minute(createdAt)) ) > :expireTime")
	int deleteExpiredRegisterUser(@Param("currentDate") Date currentDate,@Param("expireTime") Integer expireTime);

}
