package com.ak.restskillsapi.domain;

import java.util.List;

public class SaveSkillsRequest {

	private List<Long> skillsIds;
	private String userId;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Long> getSkillsIds() {
		return skillsIds;
	}
	public void setSkillsIds(List<Long> skillsIds) {
		this.skillsIds = skillsIds;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaveSkillsRequest other = (SaveSkillsRequest) obj;
		if (skillsIds != other.skillsIds)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
