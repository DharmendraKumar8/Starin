package com.starin.enums;

public enum WalletStatus {

	PENDING {
		public String toString() {
			return "PENDING";
		}
	},
	ACTIVE {
		public String toString() {
			return "ACTIVE";
		}
	},
	INACTIVE {
		public String toString() {
			return "INACTIVE";
		}
	}
}
