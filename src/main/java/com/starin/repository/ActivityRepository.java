package com.starin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starin.domain.role.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String>{
	Activity findByHandlerMethodName(String handlerMethodName);
	void deleteActivityByActivityId(Long activityId);
	Activity findByActivityId(Long activityId);
	Activity findByActivityName(String activityName);
}
