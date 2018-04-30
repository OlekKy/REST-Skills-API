package com.ak.restskillsapi.domain.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ak.restskillsapi.domain.DetailsDTO;
import com.ak.restskillsapi.domain.DetailsFullDTO;
import com.ak.restskillsapi.domain.DetailsNewDTO;
import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.repository.DetailsFullDTORepository;
import com.ak.restskillsapi.exception.DatabaseException;

@Repository
public class PostgreSQLDetailsFullDTORepository implements DetailsFullDTORepository {

	static final String url = "jdbc:postgresql://localhost:5432/restskillsdb";
	static final String user = "postgres";
	static final String password = "pass";
	
	@Autowired
	DataSource dataSource;
	
	private Connection connect() throws SQLException {	try {
		Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException e1) {
		System.out.println("Class not found " + e1);
		e1.printStackTrace();
	}
		//return DriverManager.getConnection(url, user, password);
	return dataSource.getConnection();
	}
	
	private Connection connect(boolean transactional) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, password);
		connection.setAutoCommit(transactional);
		return connection;
	}
	
	private void close(Connection connection, Statement statement) {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
		}
	}
	
	@Override
	public void createTableDetails() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = connect();
			statement = connection.createStatement();
			String sqlCreate = "CREATE TABLE IF NOT EXISTS details" 
			        + "  (id            	VARCHAR(40) PRIMARY KEY ,"
			        + "   fieldOfStudy		VARCHAR(40) ,"
			        + "	  firstName			VARCHAR(40) ,"
			        + "	  lastName			VARCHAR(40),"
			        + "	  university		VARCHAR(40),"
			        + "	  userId			VARCHAR(40) UNIQUE NOT NULL,"
			        + "   yearOfStudy		INTEGER,"
			        + "   FOREIGN KEY (userId) REFERENCES users(id)	 )";
			statement.executeUpdate(sqlCreate);
		} catch  (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("creating table Details failed", e);
		} finally {
			close(connection, statement);
		}
	}
	
	@Override
	public DetailsDTO getUserDetails(String id) {
		Connection connection = null;
		PreparedStatement statement = null;
		DetailsDTO detailsDTO = null;
		try {
			connection = connect();
			String sql = "SELECT * FROM details WHERE userId = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				detailsDTO = resultToDetails(resultSet);
			}
			statement.close();
		} catch (SQLException e) {
			throw new DatabaseException("getting user details info failed", e);
		} finally {
			close(connection, statement);
		}
		return detailsDTO;
	}

	private DetailsDTO resultToDetails(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String fieldOfStudy = resultSet.getString("fieldOfStudy");
		String firstName = resultSet.getString("firstName");
		String lastName = resultSet.getString("lastName");
		String university = resultSet.getString("university");
		int yearOfStudy = resultSet.getInt("yearOfStudy");
		DetailsDTO detailsDTO = new DetailsDTO();
		detailsDTO.setId(id);
		detailsDTO.setFieldOfStudy(fieldOfStudy);
		detailsDTO.setFirstName(firstName);
		detailsDTO.setLastName(lastName);
		detailsDTO.setUniversity(university);
		detailsDTO.setYearOfStudy(yearOfStudy);
		return detailsDTO;
	}
	
	private DetailsFullDTO resultToDetailsFull (ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String fieldOfStudy = resultSet.getString("fieldOfStudy");
		String firstName = resultSet.getString("firstName");
		String lastName = resultSet.getString("lastName");
		String university = resultSet.getString("university");
		int yearOfStudy = resultSet.getInt("yearOfStudy");
		UserFullDTO userFullDTO = (UserFullDTO)resultSet.getObject(1);
		DetailsFullDTO detailsFullDTO = new DetailsFullDTO();
		detailsFullDTO.setId(id);
		detailsFullDTO.setFieldOfStudy(fieldOfStudy);
		detailsFullDTO.setFirstName(firstName);
		detailsFullDTO.setLastName(lastName);
		detailsFullDTO.setUniversity(university);
		detailsFullDTO.setYearOfStudy(yearOfStudy);
		return detailsFullDTO;
	}
	private UserFullDTO resultToUserFull (ResultSet resultSet1) throws SQLException {
		String id = resultSet1.getString("id");
		String email = resultSet1.getString("email");
		String name = resultSet1.getString("name");
		String password = resultSet1.getString("password");
		UserFullDTO user = new UserFullDTO();
		user.setId(id);
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		return user;
	}
	@Override
	public void UpdateUserDetails(DetailsNewDTO detailsNewDTO, String id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connect();
			String sql = "INSERT INTO details (id, fieldOfStudy, firstName, lastName, university, yearOfStudy, userId) "
					+ "VALUES (?,?,?,?,?,?,?) "
					+ "ON CONFLICT (userId) DO UPDATE "
					+ " SET " 
					+ " 	fieldOfStudy = 	excluded.fieldOfStudy,"
					+ "		firstName = 	excluded.firstName,"
					+ "		lastName =		excluded.lastName,"
					+ "		university = 	excluded.university,"
					+ "		yearOfStudy = 	excluded.yearOfStudy";
			
			statement = connection.prepareStatement(sql);
			statement.setString(1, id+"a");
			statement.setString(2, detailsNewDTO.getFieldOfStudy());
			statement.setString(3, detailsNewDTO.getFirstName());
			statement.setString(4, detailsNewDTO.getLastName());
			statement.setString(5, detailsNewDTO.getUniversity());
			statement.setInt(6, detailsNewDTO.getYearOfStudy());
			statement.setString(7, id);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException("update user's details failed", e);
		} finally {
			close(connection, statement);
		}
		
	}

	@Override
	public DetailsFullDTO getAllUserDetails(String userId) {
		Connection connection = null;
		PreparedStatement statement = null;
		DetailsFullDTO detailsFullDTO = null;
		UserFullDTO userFullDTO = null;
		DetailsDTO detailsDTO = null;
		List<SkillDTO> listOfSkills = new ArrayList<SkillDTO>();
		try {
			connection = connect(false);
			String sql = "SELECT * FROM details WHERE userId = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				detailsDTO = resultToDetails(resultSet);
			}
			statement.close();
			detailsFullDTO = mapToDetailsFullDTO(detailsDTO);
			
			sql = "SELECT * FROM users WHERE id = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet resultSet1 = statement.executeQuery();
			if (resultSet1.next()) {
				userFullDTO = resultToUserFull(resultSet1);
			}
			statement.close();
			
			sql = "SELECT skills.id,skills.skillName FROM userSkills JOIN skills ON userSkills.skillId = skills.id WHERE userSkills.userId =?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet resultSet2 = statement.executeQuery();

			
			while (resultSet2.next()) {
				SkillDTO skillDTO = resultToSkill(resultSet2);
				listOfSkills.add(skillDTO);
			}
			statement.close();
			
			userFullDTO.setSkills(listOfSkills);
			detailsFullDTO.setUser(userFullDTO);
			
			connection.commit();
		} catch (SQLException e) {
			throw new DatabaseException("getting full user details info failed", e);
		} finally {
			close(connection, statement);
		}
		return detailsFullDTO;
	}

	private SkillDTO resultToSkill(ResultSet resultSet2) throws SQLException {
		int id = resultSet2.getInt("id");
		String skillName = resultSet2.getString("skillName");	
		SkillDTO skill = new SkillDTO(); 
		skill.setId(id);
		skill.setSkillName(skillName);
		return skill;
	}

	private DetailsFullDTO mapToDetailsFullDTO(DetailsDTO detailsDTO) {
		DetailsFullDTO detailsFullDTO = new DetailsFullDTO();
		detailsFullDTO.setFieldOfStudy(detailsDTO.getFieldOfStudy());
		detailsFullDTO.setFirstName(detailsDTO.getFirstName());
		detailsFullDTO.setId(detailsDTO.getId());
		detailsFullDTO.setLastName(detailsDTO.getLastName());
		detailsFullDTO.setUniversity(detailsDTO.getUniversity());
		detailsFullDTO.setYearOfStudy(detailsDTO.getYearOfStudy());
		return detailsFullDTO;
	}
	
	
}
