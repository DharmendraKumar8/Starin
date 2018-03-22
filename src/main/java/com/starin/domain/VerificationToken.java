package com.starin.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.User;



@Entity
@Table(name="verification_token")
public class VerificationToken {

	/*
	 * id used as primary key
	 */

	@Column(name="token")
	@NotNull
	
	private String token;
	@Id
	@GeneratedValue
	@Column(name="t_id")
	int id;
	
	@OneToOne
	@JoinColumn(name="u_id")
	private User user;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="registerUser_ID")
	
	public RegisterUser registerUser;
	
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	public VerificationToken(){};
	
	public VerificationToken(String token,Date date){
		this.token=token;
		this.creationDate=date;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public RegisterUser getRegisterUser() {
		return registerUser;
	}
	public void setRegisterUser(RegisterUser registerUser) {
		this.registerUser = registerUser;
	}
	@Column(name="deleted")
	private boolean deleted=false;
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
