package com.ak.restskillsapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.SkillNewDTO;
import com.ak.restskillsapi.service.SkillService;


@Controller
public class SkillController {

	@Autowired
	private SkillService skillService;
	
	@RequestMapping(value = "/skills", method = RequestMethod.GET)
	public @ResponseBody List<SkillDTO> getAllSkills() {
		return skillService.getAllSkills();
	}
	
	@RequestMapping(value = "/skills", method = RequestMethod.POST)
	public @ResponseBody void addNewSkill(@RequestBody SkillNewDTO skillNewDTO) {
		skillService.addNewSkill(skillNewDTO); 
	}
}
