package com.starin.service.user;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.starin.domain.user.LoginHistory;
import com.starin.repository.user.LoginHistoryRepository;

@Service
public class LoginHistoryService {

	private static final Logger logger = LoggerFactory.getLogger(LoginHistoryService.class);

	
	@Autowired
	LoginHistoryRepository loginHistoryRepository;
	
	public void saveLoginHistory(LoginHistory loginHistory){
		try{
		    logger.debug("saving login history");
		    loginHistoryRepository.save(loginHistory);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	public List<LoginHistory> getUserLoginHistory(String email,int limit){
		try{
			logger.debug("fetching loginHistory for user with limit");
		return loginHistoryRepository.userLoginHistory(email,new PageRequest(0, limit));
		}catch(Exception e){
			logger.error("Exception in fetching LoginHistory : ", e);
		return null;
		}
	}
	
	public String getlastLogin(Integer userid){
		List<LoginHistory> loginHistory=loginHistoryRepository.getLastLoginTimeWithIp(userid,new PageRequest(0, 1));
		return (!loginHistory.isEmpty()) ? loginHistory.get(0).loginTime()+" from IP "+loginHistory.get(0).getLoginIP() : "";
	}
	
}
