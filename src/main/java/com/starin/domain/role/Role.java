package com.starin.domain.role;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.starin.domain.generalfield.UUIDField;


@Entity
@Table(name = "Role",uniqueConstraints = @UniqueConstraint(
		columnNames = { "roleName"}))
public class Role extends UUIDField{
	 
	/*
	 * roleId is used as priamary key	
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;
	@NotNull
	private String roleName;
	private String roleDescription;
	private boolean active = true;
	
	@Column(columnDefinition="json")
	private String menuJson;

	
	public Role(){
		super(UUID.randomUUID().toString());
	}

	public String getMenuJson() {
		return menuJson;
	}

	public void setMenuJson(String menuJson) {
		this.menuJson = menuJson;
	}

	public Role(String roleName){
		super(UUID.randomUUID().toString());
		this.roleName = roleName;
	}

	public Role(String roleName,String roleDescription,boolean active){
		super(UUID.randomUUID().toString());
		this.roleName = roleName;
		this.roleDescription = roleDescription;
		this.active = active;
		
	}
	@Override
	public String toString(){
		return roleName;
	}
	
	@OneToMany(mappedBy="role",cascade = CascadeType.ALL)
	private Set<RoleActivity> activities = new LinkedHashSet<RoleActivity>(); 

	public Set<RoleActivity> getActivities() {
		return activities;
	}

	public void setActivities(Set<RoleActivity> activities) {
		this.activities = activities;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean canPerformAction(String handlerMethodName){
		for(RoleActivity roleActivity: this.activities){
			if(roleActivity.getActivity().getHandlerMethodName().equals(handlerMethodName)){
				return true;
			}
		}
		return false;
	}

}
