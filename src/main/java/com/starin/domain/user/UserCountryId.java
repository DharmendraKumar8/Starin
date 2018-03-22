package com.starin.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserCountryId implements Serializable{

	@Column(name = "userId")
	private Integer userId;

	@Column(name = "countryId")
	private Integer countryId;

	public UserCountryId() { }
		
	public UserCountryId(Integer userId, Integer countryId) {
		this.userId = userId;
		this.countryId = countryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((countryId == null) ? 0 : countryId.hashCode());
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
		
		UserCountryId other = (UserCountryId) obj;
		
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		
		return true;
	}
}
	
