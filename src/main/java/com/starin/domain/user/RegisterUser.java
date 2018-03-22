package com.starin.domain.user;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.starin.domain.generalfield.UUIDField;
import com.starin.domain.role.Role;
import com.starin.utils.KYCUtilities;


@Entity
@Table(name = "registerUser")
public class RegisterUser extends UUIDField {
	  
		/*
		 * registerUserId is the primary key
		 */
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="registerUserId")
		public Integer registerUserId;
		
		public Integer getRegisterUserId() {
			return registerUserId;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}

		public void setRegisterUserId(Integer registerUserId) {
			this.registerUserId = registerUserId;
		}
		@Temporal(TemporalType.TIMESTAMP)
		private Date createdAt;
			
		@Column(name="email",unique=true)
		@NotNull(message="email can not be null")
		@Size(min=1,message="email can not be empty")
		private String email;

		@NotNull(message="password can not be null")
		@Column(name="password")
		private String password;
			
		@NotNull(message="name can not be null")
		@Column(name="name")	
		private String name;		

		@ManyToOne
		@JoinColumn(name = "roleid", nullable = false)
		private Role role;

		@NotNull(message="countryId can not be null")
		private Integer countryID;		

		public Integer getCountryID() {
			return countryID;
		}

		public void setCountryID(Integer countryID) {
			this.countryID = countryID;
		}

		public RegisterUser(){
			super(UUID.randomUUID().toString());
		}

		public RegisterUser(String name,String password,String email,Role role,Date createdAt,Integer countryID){
			super(UUID.randomUUID().toString());
			this.name=name;	
			this.password=password;
			this.email=email;		
			this.role=role;		
			this.createdAt=createdAt;
			this.countryID=countryID;
		}


		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = KYCUtilities.bcryptEncryptor(password.trim());
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

}
