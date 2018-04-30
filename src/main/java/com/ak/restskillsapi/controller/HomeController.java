package com.ak.restskillsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ak.restskillsapi.domain.repository.DetailsFullDTORepository;
import com.ak.restskillsapi.domain.repository.SkillDTORepository;
import com.ak.restskillsapi.domain.repository.UserFullDTORepository;

@Controller
public class HomeController {

	@Autowired
	private SkillDTORepository skillDTORepository;
	
	@Autowired 
	private UserFullDTORepository userFullDTORepository;
	
	@Autowired 
	private DetailsFullDTORepository detailsFullDTORepository;
	
	@RequestMapping("/")
	public String createAllTables() {
		skillDTORepository.createTableSkills();
		userFullDTORepository.createTableUsers();
		userFullDTORepository.createTableUserSkills();
		detailsFullDTORepository.createTableDetails();
		return null;
	}
}
