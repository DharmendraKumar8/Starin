package com.starin.domain.user;


import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.starin.domain.generalfield.UUIDField;
import com.starin.utils.ObjectHash;


@Entity
public class LoginHistory extends UUIDField{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int loginHistoryId;
	
	private String loginIP;
	
	private String userAgent;
	
	private Date loginTime;
	
	@ManyToOne
	@JoinColumn(name = "UserID", nullable = true)
	private User user;

	public LoginHistory(){
		super(UUID.randomUUID().toString());
	}
	
	public LoginHistory(Date loginTime, String loginIP, String userAgent) {
		super(UUID.randomUUID().toString());
		this.loginTime = loginTime;
		this.loginIP = loginIP;
		this.userAgent = userAgent;
	}
	
	public int getLoginHistoryId() {
		return loginHistoryId;
	}

	public void setLoginHistoryId(int loginHistoryId) {
		this.loginHistoryId = loginHistoryId;
	}

	public Date getLoginTime() {
		return loginTime;
	}
	
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ObjectHash
	public String loginTime(){
		//return KYCDateUtil.convertDateInToUTCTimeZone(this.loginTime);
		return this.loginTime.toString()+"Z";
	}
	
}
