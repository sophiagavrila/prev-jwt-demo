package com.revature.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.Role;
import com.revature.model.Roles;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByRoleName(Roles role);

}
