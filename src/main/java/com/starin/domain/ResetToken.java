package com.starin.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.starin.domain.user.User;

@Entity
@Table(name = "ResetToken")
public class ResetToken {

	/*
	 * id used as primary key
	 */

	@Column(name = "a_id")
	@Id
	@GeneratedValue
	private int id;

	@Column(name = "token")
	private String token;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "u_id")
	private User user;

	@Column(name = "creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "deleted")
	private boolean deleted = false;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
