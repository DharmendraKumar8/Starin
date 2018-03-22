package com.starin.domain.role;

import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.starin.domain.generalfield.UUIDField;
import com.starin.utils.ObjectHash;

@Entity
public class RoleActivity extends UUIDField{

	@ObjectHash
	@EmbeddedId
	private RoleActivityId roleActivityId;
	
	@ManyToOne
	@JoinColumn(name="roleId",updatable = false,insertable=false)
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="activityId",updatable = false,insertable=false)
	private Activity activity;
	
	public RoleActivity() {super(UUID.randomUUID().toString());}
		
	public RoleActivity(Role role,Activity activity) {
		super(UUID.randomUUID().toString());
		this.roleActivityId = new RoleActivityId(role.getRoleId(),activity.getActivityId());
		this.role = role;
		this.activity = activity;
		
		this.role.getActivities().add(this);
		this.activity.getRoles().add(this);
			
	}
	
	public RoleActivityId getRoleActivityId() {
		return roleActivityId;
	}

	public void setRoleActivityId(RoleActivityId roleActivityId) {
		this.roleActivityId = roleActivityId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
