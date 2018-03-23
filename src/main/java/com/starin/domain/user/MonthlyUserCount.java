package com.starin.domain.user;

public class MonthlyUserCount {

	private String users;
	private String month_name;
	private String year;

	public MonthlyUserCount(String users, String month_name, String year) {
		this.users=users;
		this.month_name=month_name;
		this.year=year;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getUsers() {
		return users;
	}


	public void setUsers(String users) {
		this.users = users;
	}


	public String getMonth_name() {
		return month_name;
	}

	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}


}
