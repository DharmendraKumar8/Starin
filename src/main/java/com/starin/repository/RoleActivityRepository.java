package com.starin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.starin.domain.role.Activity;
import com.starin.domain.role.Role;
import com.starin.domain.role.RoleActivity;
import com.starin.domain.role.RoleActivityId;

@Repository
public interface RoleActivityRepository extends JpaRepository<RoleActivity, RoleActivityId> {

	@Query("select activity from RoleActivity ra where ra.role.roleId=:roleId and ra.activity.activityId=:activityId")
	Activity getActivityByRole(@Param("roleId") Long roleId, @Param("activityId") Long activityId);

	@Query("select activity from RoleActivity ra where ra.role.roleId=:roleId")
	List<Activity> findAllByRole(@Param("roleId") Long roleId);

	Integer deleteByRole(Role role);
}
