package com.starin.domain.user;

public class DailyUserCount {

	private String date;

	private int users;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	public DailyUserCount(String date, int users) {
		this.date = date;
		this.users = users;
	}
}
