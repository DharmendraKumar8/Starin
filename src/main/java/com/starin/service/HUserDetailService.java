package com.starin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starin.domain.user.HUserDetail;
import com.starin.repository.user.HUserDetailRepository;

@Service
public class HUserDetailService {

	@Autowired
	private HUserDetailRepository hUserDetailRepository;

	private static final Logger logger = LoggerFactory.getLogger(HUserDetailService.class);

	public HUserDetailService() {
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public HUserDetail saveHUserDetail(HUserDetail hUserDetail) {
		try {
			return hUserDetailRepository.save(hUserDetail);
		} catch (Exception e) {
			logger.error("Exception occur while try to save HUserDetail: ", e);
			return null;
		}
	}

}
