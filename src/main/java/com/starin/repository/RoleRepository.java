package com.starin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starin.domain.role.Role;




@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
	Role findByRoleName(String roleName);
	Integer deleteRoleByRoleName(String roleName);
	Role findByRoleId(Long roleId);
	Role[] findByRoleNameIn(List<String> rolenames);
}
