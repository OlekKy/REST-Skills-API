package com.ak.restskillsapi.service;

import java.util.List;

import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.SkillNewDTO;

public interface SkillService {

	List<SkillDTO> getAllSkills();
	void addNewSkill(SkillNewDTO skillNewDTO);
}
