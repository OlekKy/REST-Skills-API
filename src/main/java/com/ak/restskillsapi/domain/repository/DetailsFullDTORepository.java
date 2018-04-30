package com.ak.restskillsapi.domain.repository;

import com.ak.restskillsapi.domain.DetailsDTO;
import com.ak.restskillsapi.domain.DetailsFullDTO;
import com.ak.restskillsapi.domain.DetailsNewDTO;

public interface DetailsFullDTORepository {

	void createTableDetails();
	DetailsDTO getUserDetails(String id);
	void UpdateUserDetails(DetailsNewDTO detailsNewDTO, String id);
	DetailsFullDTO getAllUserDetails(String userId);
}
