package com.starin.enums;

public enum FieldType {

	text("text"), date("date");

	private String value;

	private FieldType(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

}
