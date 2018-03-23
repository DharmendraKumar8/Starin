package com.starin.service.token;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.conf.EnvConfiguration;

@Service
public class TokenServiceImpl implements TokenService{	
	
	@Autowired
	public EnvConfiguration configuration;
	
	@Override
	public String genrate() {
		return UUID.randomUUID().toString();
	}

}
