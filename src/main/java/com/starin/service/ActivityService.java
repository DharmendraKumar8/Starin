package com.starin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starin.conf.EnvConfiguration;
import com.starin.domain.role.Activity;
import com.starin.domain.role.Role;
import com.starin.domain.role.RoleActivity;
import com.starin.domain.role.RoleActivityId;
import com.starin.repository.ActivityRepository;
import com.starin.repository.RoleActivityRepository;
import com.starin.utils.KYCRoleUtil;
import com.starin.utils.KYCUtilities;


/**
 * Service For CRUD Operation for Acitvity Entity  
  *
 */
@Service
public class ActivityService {

	private final Logger log = LoggerFactory.getLogger(ActivityService.class);

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired 
	private EnvConfiguration configuration;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleActivityService roleActivityService;

	@Autowired
	private RoleActivityRepository roleactivityrepository;

	public ActivityService(){}

	/**
	 *  This method is called after bean
	 *  properties initialization to insert 
	 *  activities entries in database 
	 */

	@PostConstruct
	public void init() {
		log.debug("initilizing activities");
		intializeActivities(configuration.getControllerPackages());
		log.debug("Activites initializing for admin");
		//Fetching activities available in database
		if(configuration.getAssignAllActivityToAdminFlag()){
			List<Activity> available=getAllActivities();
			if(available==null){
				log.debug("No Activity in the database unusual case,please look into it");
				//system.exit(-1);
			}else{
				Role adminRole=roleService.findByRoleName(configuration.getAdminRoleName());

				if(adminRole==null){
					log.error("ADMIN ROLE NOT FOUND,Kindly restart the server after resolving the issue");
					return;
					//System.exit(-1);
				}else{
					List<Activity> adminActivity=null;
					Set<Activity> activitySet =null;
					try{
						adminActivity=roleactivityrepository.findAllByRole(adminRole.getRoleId());
						activitySet = new HashSet<Activity>(activityRepository.findAll());
					}catch(Exception e){
						log.error("Error in assigning activity to admin please see stack trace and resolve it ASAP",e);
						return;
					}
					if(adminActivity.isEmpty()){
						for(Activity activity : activitySet){
							RoleActivity roleActivity = new RoleActivity();
							roleActivity.setRoleActivityId(new RoleActivityId(adminRole.getRoleId(),activity.getActivityId()));
							roleActivity.setRole(adminRole);
							roleActivity.setActivity(activity);
							try{
								roleactivityrepository.save(roleActivity);
							}catch(Exception e){
								log.error("Exception occur while save RoleActivity durring adminUser Creation .", e);
							}
						}
					}else{
						Set<Long> adminActivityIdSet=new HashSet<Long>();
						for(Activity acitvity:adminActivity){
							adminActivityIdSet.add(acitvity.getActivityId().longValue());
						}

						for(Activity activity:activitySet){
							if(!adminActivityIdSet.contains(activity.getActivityId().longValue())){
								RoleActivity roleActivity = new RoleActivity();
								roleActivity.setRoleActivityId(new RoleActivityId(adminRole.getRoleId(),activity.getActivityId()));
								roleActivity.setRole(adminRole);
								roleActivity.setActivity(activity);
								try{
									roleactivityrepository.save(roleActivity);
								}catch(Exception e){
									log.error("Exception occur while save RoleActivity durring adminUser Creation .", e);
								}
							}
						}
					}
				}


			}
		}else{
			log.debug(" allActivityToAdmin flag is false ,so not assigning all activity to admin by default");
		}

	}

	public void intializeActivities(String[] packageNames){
		List<Activity> activities = KYCUtilities.getAllActivityObject(packageNames);
		log.debug((activities != null ? activities.size() : activities)+" activites read from  @Activty");
		List<Activity> dbActivityArray = getAllActivities();
		//TO DO null pointer
		log.debug((dbActivityArray != null  ? dbActivityArray.size() : dbActivityArray)+" activites read from  Database");

		if(dbActivityArray == null)
			return;

		List<Activity> uniqueActivtyArray =  KYCUtilities.getListA_Minus_ListBObjects(activities, dbActivityArray, "handlerMethodName");
		log.debug(uniqueActivtyArray.size()+" new activites are created for insert into database.");
		for(Activity activity : uniqueActivtyArray){
			activity=persistActivity(activity);
		}
		log.debug("activites inserted into database Sucessfullly");
	}

	@Transactional
	public Activity persistActivity(Activity activity){
		try{
			activity =  activityRepository.save(activity);
		}catch(Exception e){
			log.error("Activity not Saved "+e);
			return null;
		}
		return activity;
	}

	public Activity findByHandlerMethodName(String handlerMethodName){
		return this.activityRepository.findByHandlerMethodName(handlerMethodName);
	}

	public Activity findByActivityName(String activityName){
		return this.activityRepository.findByActivityName(activityName);
	}

	public Activity findByActivityId(Long activityId){
		return this.activityRepository.findByActivityId(activityId);
	}

	@Transactional
	public boolean deleteActivityByActivityId(Long activityId){
		boolean isSucces = true;
		try{
			activityRepository.deleteActivityByActivityId(activityId);
		}catch(Exception e){
			isSucces = false;
			log.error("Activity not Deleted "+e.getMessage());
		}
		return isSucces;
	}

	public List<Activity> getAllActivities(){
		log.debug("returing all activities list");
		try{
			return activityRepository.findAll();
		}catch(Exception e){
			log.debug("Exception occure while fetch all activities from database : "+e);
			return null;			
		}
	}
	public List<Activity> findAll(Role role){
		List<com.starin.domain.role.Activity> activityList = new ArrayList<com.starin.domain.role.Activity>();
		if(KYCRoleUtil.level(role.getRoleName())==1)
			activityList=new ArrayList<com.starin.domain.role.Activity>(this.activityRepository.findAll());
		else{
			for(RoleActivity roleActivity : role.getActivities())
			{
				activityList.add(roleActivity.getActivity());
			}
		}
		return activityList;
	}

	public List<Activity> findAll(){
		return activityRepository.findAll();
	}

	public Map<String,Object> assginActivitybySuperUser(Long ids[]){
		long count=0;
		Set<com.starin.domain.role.Activity> activities = new LinkedHashSet<>();
		Map<String,Object> map=null;
		for(Long activityId : ids){
			Activity activity=activityRepository.findByActivityId(activityId);
			if(activity==null){
				map=new HashMap<String,Object>();
				map.put("error",activityId);
				return map;
			}
			activities.add(activity);
			count++;
		}
		map=new HashMap<String,Object>();
		map.put("count",count);
		map.put("activities",activities);
		return map;
	}

	public Map<String,Object> assignActivty(Long ids[],Long roleId){
		long count=0;
		Set<com.starin.domain.role.Activity> activities = new LinkedHashSet<>();
		Map<String,Object> map=null;
		for(Long activityId : ids){
			//	List<Object[]> role_Activites=roleService.getRoleActivites(roleid,activityId);
			Activity activityOfRole = roleActivityService.getActivityByRole(roleId, activityId);
			if(activityOfRole == null){
				map=new HashMap<String,Object>();
				map.put("error",activityId);
				return map;
			}
			//	com.oodles.domain.role.Activity activity = findByActivityId(activityId);
			count++;
			activities.add(activityOfRole);
		}
		map=new HashMap<String,Object>();
		map.put("count",count);
		map.put("activities",activities);
		return map;
	}

}
