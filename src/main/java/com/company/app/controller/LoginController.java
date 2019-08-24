package com.company.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.app.model.Profile;
import com.company.app.service.ProfileService;
import com.google.gson.Gson;


@CrossOrigin(origins = "http://localhost:4200")
//@RestController
@Controller
public class LoginController {
		
	@Autowired
	private ProfileService profileService;
	
	@RequestMapping(value = "/dashboard")
	public String dasborard(Model model,Authentication authentication) {
		
		// Se imprime en consola los detalles del usuario que inició sesión
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		System.out.println("--------- User has authorities: " + userDetails.getAuthorities());
				
		// Se obtienen los perfiles de usuario para cargar en el combo
		List<Profile> profileslist = profileService.getProfiles();
		model.addAttribute("profiles", profileslist);
	
		return "dashboard";
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		//List<Profile> profileslist = profileService.getProfiles();
		//String json = new Gson().toJson(profileslist);
		//return json;
		return "login";
	}
	
	@RequestMapping(value = "/invalidSession")
	public String invalidSession() {
		return "login";
	}
	
	@RequestMapping(value = "/")
	public String root() {
		return "login";
	}
	
	@RequestMapping(value="/users")
	public String getUsers() {
		return "users";	
	}
	
	@RequestMapping(value="/expiredSession")
	public String expiredSession() {
		return "expiredSession";
		/*List<Profile> profileslist = profileService.getProfiles();
		String json = new Gson().toJson(profileslist);
		return json;*/
		
	}
}
