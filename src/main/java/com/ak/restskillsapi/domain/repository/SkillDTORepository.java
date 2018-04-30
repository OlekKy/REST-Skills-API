package com.ak.restskillsapi.domain.repository;

import java.util.List;

import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.SkillNewDTO;

public interface SkillDTORepository {

	void createTableSkills();
	List<SkillDTO> getAllSkills();
	void addNewSkill(SkillNewDTO skillNewDTO);
}
