package com.starin.service.token;


import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.domain.AuthenticationToken;
import com.starin.repository.token.AuthTokenRepository;

/*
 * Service For CRUD Operation for AuthenticationToken Entity  
 */

@Service
@Transactional
public class AuthTokenService {

	private final Logger log = LoggerFactory.getLogger(AuthTokenService.class);


	@Autowired
	private AuthTokenRepository authtokenrepo;

	public AuthenticationToken findByToken(String token){
		log.debug("Fetching authentication token entity by token hash");
		try{
			return authtokenrepo.findByToken(token);
		}catch (Exception e) {
			log.error("Exception occur while fetch AuthenticationToken : ",e);
			return null;
		}
	}

	public AuthenticationToken save(AuthenticationToken token){
		log.debug("Saving authentication token in database");
		return authtokenrepo.save(token);
	}
	//-1 is when token does not exists and for success 0
	public int logout(String authToken){
		if(authToken.equals(""))
			return -1;

		AuthenticationToken token=authtokenrepo.findByToken(authToken);
		if(token==null)
			return -1;
		else{
			authtokenrepo.delete(token);
			return 0;
		}
	}
	public AuthenticationToken findByUserUid(Integer uid){
		return authtokenrepo.findByUserUid(uid);
	}
}
