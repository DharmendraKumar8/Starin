package com.starin.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starin.domain.AuthenticationToken;


@Repository
public interface AuthTokenRepository extends JpaRepository<AuthenticationToken,Integer>{

	AuthenticationToken findByToken(String token);
	
	AuthenticationToken findByUserUid(Integer uid);

}
