package com.ak.restskillsapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ak.restskillsapi.domain.DetailsDTO;
import com.ak.restskillsapi.domain.DetailsFullDTO;
import com.ak.restskillsapi.domain.DetailsNewDTO;
import com.ak.restskillsapi.domain.SaveSkillsRequest;
import com.ak.restskillsapi.domain.UserCreatedDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.UserNewDTO;
import com.ak.restskillsapi.domain.repository.DetailsFullDTORepository;
import com.ak.restskillsapi.domain.repository.UserFullDTORepository;
import com.ak.restskillsapi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserFullDTORepository userFullDTORepository;
	
	@Autowired
	private DetailsFullDTORepository detailsFullDTORepository;
	
	@Override
	public void createNewUser(UserNewDTO userNewDTO, String sessionId, String pass) {
		userFullDTORepository.createNewUser(userNewDTO, sessionId, pass);
	}

	@Override
	public UserCreatedDTO retrieveUser(String id) {
		
		return userFullDTORepository.retrieveUser(id);
	}

	@Override
	public UserFullDTO addSkillsToUser(SaveSkillsRequest saveSkillsRequest) {
		return userFullDTORepository.addSkillsToUser(saveSkillsRequest);
	}

	@Override
	public DetailsDTO getUserDetails(String id) {
		return detailsFullDTORepository.getUserDetails(id);
	}

	@Override
	public void updateUserDetails(DetailsNewDTO detailsNewDTO, String id) {
		detailsFullDTORepository.UpdateUserDetails(detailsNewDTO, id);
	}

	@Override
	public DetailsFullDTO getAllUserDetails(String userId) {
		return detailsFullDTORepository.getAllUserDetails(userId);
	}

}
