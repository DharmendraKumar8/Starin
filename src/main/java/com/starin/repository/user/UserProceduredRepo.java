package com.starin.repository.user;

import java.util.List;

import com.starin.domain.user.DailyUserCount;
import com.starin.domain.user.MonthlyUserCount;
import com.starin.domain.user.YearlyUserCount;


public interface UserProceduredRepo {
	List<MonthlyUserCount> monthlyRegisteredUser(String startDate,String endDate);
	List<YearlyUserCount> yearlyRegisteredUser(String startDate,String endDate);
	List<DailyUserCount> dailyRegisteredUser(String startDate,String endDate);
}
