package com.starin.service.token;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.domain.ResetToken;
import com.starin.repository.token.ResetTokenRepository;

/**
 * Service For CRUD Operation for AuthenticationToken Entity
 */

@Service
public class ResetTokenService {
	@Autowired
	private ResetTokenRepository resetTokenRepository;

	private static final Logger logger = LoggerFactory.getLogger(ResetTokenService.class);

	@Transactional
	public ResetToken findByToken(String token) {
		logger.debug("finding ResetToken by token hash");
		return resetTokenRepository.findByToken(token);
	}

	@Transactional
	public ResetToken save(ResetToken token) {
		logger.debug("saving reset token");
		return resetTokenRepository.save(token);
	}

	@Transactional
	public void deleteOldTokens(int uid) {
		logger.debug("deleting old reset tokens related to user");
		resetTokenRepository.deleteOldTokens(uid);
	}

}
