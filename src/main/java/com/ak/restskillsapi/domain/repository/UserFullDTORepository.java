package com.ak.restskillsapi.domain.repository;

import com.ak.restskillsapi.domain.SaveSkillsRequest;
import com.ak.restskillsapi.domain.UserCreatedDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.UserNewDTO;

public interface UserFullDTORepository {

	void createTableUsers();
	void createTableUserSkills();
	void createNewUser(UserNewDTO userNewDTO, String sessionId, String pass);
	UserCreatedDTO retrieveUser(String id);
	UserFullDTO addSkillsToUser(SaveSkillsRequest saveSkillsRequest);

}
