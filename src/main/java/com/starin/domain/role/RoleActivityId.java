package com.starin.domain.role;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class RoleActivityId implements Serializable{

	

	@Column(name="roleId")
	private Long roleId;
	
	@Column(name="activityId")
	private Long activityId;

	public RoleActivityId(){} 

	public RoleActivityId(Long roleId,Long activityId){
		this.roleId = roleId;
		this.activityId = activityId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result
				+ ((activityId == null) ? 0 : activityId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RoleActivityId other = (RoleActivityId) obj;
		
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		
		if (activityId == null) {
			if (other.activityId != null)
				return false;
		} else if (!activityId.equals(other.activityId))
			return false;
		
		return true;
	}

}
