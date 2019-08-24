package com.company.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.app.model.Profile;
import com.company.app.repository.ProfileRepository;


@Service
public class ProfileService {

	@Autowired
	private ProfileRepository repository;
	
	public List<Profile> getProfiles(){
		return repository.findAll();
		
	}
	@Transactional
	public void saveProfile(Profile profile){
		repository.save(profile);
	}
}
