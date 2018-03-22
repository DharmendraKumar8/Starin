package com.starin.domain.role;

import org.springframework.stereotype.Component;

/*
 * This the default configuration of super user role
 */

@Component
final public class SuperUserRole {
	
	final private String rolename="superadmin";
	final private boolean active=true;
	private String description="This Role Grants complete control over KycManagment System";
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRolename() {
		return rolename;
	}
	public boolean isActive() {
		return active;
	}
	
}