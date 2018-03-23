package com.starin.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starin.domain.VerificationToken;
import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.User;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Integer>{
	 VerificationToken findByToken(String token);
	 VerificationToken findByUser(User user);
	 VerificationToken findByRegisterUser(RegisterUser user);
}
