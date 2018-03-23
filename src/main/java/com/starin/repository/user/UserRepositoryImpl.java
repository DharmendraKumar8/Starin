package com.starin.repository.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import com.starin.domain.user.DailyUserCount;
import com.starin.domain.user.MonthlyUserCount;
import com.starin.domain.user.YearlyUserCount;

public class UserRepositoryImpl implements UserProceduredRepo {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<MonthlyUserCount> monthlyRegisteredUser(String startDate, String endDate) {
		
		 List<Object[]> storedProcedureResults =durationMethod(startDate,endDate,"month");
		 return storedProcedureResults.stream().map(result -> new MonthlyUserCount(
		          result[0].toString(),
		          result[1].toString(),
		          result[2].toString()
		   )).collect(Collectors.toList());
	}
	
	@Override
	public List<YearlyUserCount> yearlyRegisteredUser(String startDate, String endDate) {
		
		 List<Object[]> storedProcedureResults =durationMethod(startDate,endDate,"year");
		 return storedProcedureResults.stream().map(result -> new YearlyUserCount(
		         Integer.parseInt(result[0].toString()),
		         Integer.parseInt(result[1].toString())
		         
		   )).collect(Collectors.toList());
	}
	
	public List<Object[]> durationMethod(String startDate, String endDate,String order){
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("belrium.reg_freq");
		 String firstParam="firstParam";
		 String secondParam="secondParam";
		 String thirdParam="thirdParam";
		 storedProcedure.registerStoredProcedureParameter(firstParam, String.class, ParameterMode.IN);
		 storedProcedure.registerStoredProcedureParameter(secondParam, String.class, ParameterMode.IN);
		 storedProcedure.registerStoredProcedureParameter(thirdParam, String.class, ParameterMode.IN);
		 storedProcedure.setParameter(firstParam, startDate);
		 storedProcedure.setParameter(secondParam, endDate);
		 storedProcedure.setParameter(thirdParam, order);
		 return storedProcedure.getResultList();
	}

	@Override
	public List<DailyUserCount> dailyRegisteredUser(String startDate, String endDate) {
		List<Object[]> storedProcedureResults =durationMethod(startDate,endDate,"day");
		return storedProcedureResults.stream().map(result -> new DailyUserCount(
		         result[1].toString(),
		         Integer.parseInt(result[0].toString())
		   )).collect(Collectors.toList());
	}

}
