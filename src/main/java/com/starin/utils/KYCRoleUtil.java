package com.starin.utils;

import com.starin.conf.BaseConfig;

public class KYCRoleUtil {

	public static int level(String rolename) {
		return BaseConfig.roles_level.get(rolename.toLowerCase().trim()) != null
				? BaseConfig.roles_level.get(rolename.toLowerCase().trim())
				: 3;
	}

	public static int compare(String targetrole, String sourcerole) {

		int t_role = level(targetrole);
		int s_role = level(sourcerole);

		if (t_role == 1)
			return -1;
		if (t_role == 2 && s_role == 2)
			return -1;
		if (s_role == 3)
			return 0;
		return 0;
	}
}
