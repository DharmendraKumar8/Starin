
package com.starin.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starin.conf.EnvConfiguration;
import com.starin.domain.role.Role;
import com.starin.domain.role.SuperUserRole;
import com.starin.repository.RoleRepository;


@Transactional
@Service
public class RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired 
	EnvConfiguration configuration;

	@Autowired
	SuperUserRole superrole;

	public RoleService(){}

	/**
	 * This method is used to initialize roles 
	 * after fields values set in Role service
	 */

	@PostConstruct
	public void init() {
		logger.debug("initilizing roles in databases");
		intializeRoles(configuration.getRoles());
		logger.debug("role intilized");
	}

	public Role findByRoleName(String roleName){
		try{
			return this.roleRepository.findByRoleName(roleName);
		}catch(Exception e){
			logger.debug("Exception occcur while role fetch by roleId : "+e);
			return null;
		}
	}

	public Role findByRoleId(Long roleId){
		try{
			return this.roleRepository.findByRoleId(roleId);
		}catch(Exception e){
			logger.debug("Exception occcur while role fetch by roleId : "+e);
			return null;
		}
	}

	public void intializeRoles(String[] roles){
		Role superuserrole=new Role(superrole.getRolename(),superrole.getDescription(),superrole.isActive());
		this.persistRole(superuserrole);
		if(roles != null){
			for(String roleName : roles){
				Role role = new Role(roleName.trim());
				role.setRoleDescription("this is "+role.getRoleName()+" role");
				this.persistRole(role);
			}
		}
	}

	public Role saveRole(Role role) throws DataIntegrityViolationException,Exception{
		return roleRepository.save(role);
	}

	public Role persistRole(Role role){
		try{
			role = roleRepository.save(role);
		}catch(DataIntegrityViolationException e){
			logger.error("Role not Saved "+e.getMessage());
			return role;
		}catch(Exception e){
			logger.error("Role not Saved "+e.getMessage());
			return null;
		}
		return role;
	}

	public boolean deleteRoleByRoleName(String roleName){
		boolean isSucces = false;
		try{
			isSucces =	(roleRepository.deleteRoleByRoleName(roleName) > 0 ) ? true : false;
		}catch(Exception e){
			isSucces = false;
			logger.error("Role not Deleted "+e.getMessage());
		}
		return isSucces;
	}

	public List<Role> getAllRole(){
		try{
			return roleRepository.findAll();	
		}catch (Exception e) {
			logger.error("Exception occur while fetch all roles : "+e);
			return null;
		}
	}

	public List<Role> removeRole(List<Role> roles,String rolename){

		Role remove=null;
		for(Role r:roles){
			if(r.getRoleName().equalsIgnoreCase(rolename)){
				remove=r;
				break;
			}
		}
		if(remove!=null)
			roles.remove(remove);

		return roles;
	}
	public Role[] findByRolenameIn(List<String> rolenames){
		return roleRepository.findByRoleNameIn(rolenames);
	}


}
