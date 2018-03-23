package com.starin.domain.role;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.starin.domain.generalfield.UUIDField;

@Entity
@Table(name="Activity",uniqueConstraints =@UniqueConstraint(columnNames = { "handlerMethodName"}))
public class Activity extends UUIDField{

	/*
	 * activityId used as primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long activityId;
	private String activityName;
	private String activityDescription;
	private boolean active;
	//Its a method name on which @Activity annotation exist...
	private String handlerMethodName;
	
	
	@Column(name="url",length=400)
	private String url;
	
	@Column(name="method_type",length=10)
	private String methodType;
	public Activity(){super(UUID.randomUUID().toString());}

	public Activity(String activityName,String activityDescription,boolean active,String handlerMethodName){
		super(UUID.randomUUID().toString());
		this.activityName = activityName;
		this.activityDescription = activityDescription;
		this.active = active;
		this.handlerMethodName = handlerMethodName;
	}
	
	public Activity(String activityName,String activityDescription,boolean active,String handlerMethodName,String url,String methodType){
		super(UUID.randomUUID().toString());
		this.activityName = activityName;
		this.activityDescription = activityDescription;
		this.active = active;
		this.handlerMethodName = handlerMethodName;
		this.url=url;
		this.methodType=methodType;
	}

	@OneToMany(mappedBy = "activity")
	private Set<RoleActivity> roles = new LinkedHashSet<RoleActivity>();

	public Set<RoleActivity> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleActivity> roles) {
		this.roles = roles;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public String getHandlerMethodName() {
		return handlerMethodName;
	}

	public void setHandlerMethodName(String handlerMethodName) {
		this.handlerMethodName = handlerMethodName;
	}

}
