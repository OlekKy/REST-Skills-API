package com.ak.restskillsapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.SkillNewDTO;
import com.ak.restskillsapi.domain.repository.SkillDTORepository;

import com.ak.restskillsapi.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

	@Autowired
	private SkillDTORepository skillDTORepository;
	
	@Override
	public List<SkillDTO> getAllSkills() {
		return skillDTORepository.getAllSkills();
	}

	@Override
	public void addNewSkill(SkillNewDTO skillNewDTO) {
		skillDTORepository.addNewSkill(skillNewDTO);
	}

	
}
