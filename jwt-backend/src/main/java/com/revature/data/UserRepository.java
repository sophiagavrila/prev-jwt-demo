package com.revature.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.User;

// Since we are using Spring Data JPA so we just need to create interfaces extending the JpaRepository.
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	  public Optional<User> findByUserName(String userName);
	  public boolean existsByEmail(String email);
	  public boolean existsByUserName(String userName);
	
}
