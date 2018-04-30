package com.ak.restskillsapi.service;

import com.ak.restskillsapi.domain.DetailsDTO;
import com.ak.restskillsapi.domain.DetailsFullDTO;
import com.ak.restskillsapi.domain.DetailsNewDTO;
import com.ak.restskillsapi.domain.SaveSkillsRequest;
import com.ak.restskillsapi.domain.UserCreatedDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.UserNewDTO;

public interface UserService {

	void createNewUser(UserNewDTO userNewDTO, String sessionId, String pass);
	UserCreatedDTO retrieveUser(String id);
	UserFullDTO addSkillsToUser(SaveSkillsRequest saveSkillsRequest);
	DetailsDTO getUserDetails(String id);
	void updateUserDetails(DetailsNewDTO detailsNewDTO, String id);
	DetailsFullDTO getAllUserDetails(String userId);
}
