package com.starin.domain.user;

public class YearlyUserCount {
	private int users;
	private int years;

	public YearlyUserCount(int users, int years) {
		this.users = users;
		this.years = years;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}
}
