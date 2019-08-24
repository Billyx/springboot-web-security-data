package com.company.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.app.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer>{
	public List<Profile> findByDescription(String description);
	//@Query("select c from Customer c where c.email = ?1")
	//public List<Profile> findByState(String email);
}
