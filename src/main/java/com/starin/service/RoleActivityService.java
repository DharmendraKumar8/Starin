package com.starin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starin.domain.role.Activity;
import com.starin.domain.role.Role;
import com.starin.domain.role.RoleActivity;
import com.starin.domain.role.RoleActivityId;
import com.starin.repository.RoleActivityRepository;

@Service
public class RoleActivityService {

	@Autowired
	private RoleActivityRepository roleActivityRepository;

	private static final Logger logger = LoggerFactory.getLogger(RoleActivityService.class);

	public RoleActivityService() {
	}

	public RoleActivity save(RoleActivity roleActivity) {
		return roleActivityRepository.save(roleActivity);
	}

	public RoleActivity persistRoleActivity(RoleActivity roleActivity) {

		logger.info("inside persistRoleActivity try to Persist Role Activity ");
		try {
			return save(roleActivity);
		} catch (Exception e) {
			logger.error("Exception occur while persist RoleActivity : ", e);
			return null;
		}
	}

	public Activity getActivityByRole(Long roleId, Long activityId) {
		try {
			return roleActivityRepository.getActivityByRole(roleId, activityId);
		} catch (Exception e) {
			logger.error("Exception occur while fetch Activity by roleId and activtyId from RoleActivity : ", e);
			return null;
		}
	}

	@Transactional
	public Integer deleteByRole(Role role) throws Exception {
		try {
			return roleActivityRepository.deleteByRole(role);
		} catch (Exception e) {
			logger.error("Exception occur while delete RoleActivity by role : ", e);
			return null;
		}
	}

	public RoleActivity addRoleActivity(Role role, Activity activity) {

		RoleActivity roleActivity = new RoleActivity();
		roleActivity.setRoleActivityId(new RoleActivityId(role.getRoleId(), activity.getActivityId()));
		roleActivity.setRole(role);
		roleActivity.setActivity(activity);
		try {
			return save(roleActivity);
		} catch (Exception e) {
			logger.error("Exception occur while Add new RoleActivity : ", e);
			return null;
		}
	}

}
