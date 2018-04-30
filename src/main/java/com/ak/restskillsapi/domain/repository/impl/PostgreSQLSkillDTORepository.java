package com.ak.restskillsapi.domain.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ak.restskillsapi.domain.SkillDTO;
import com.ak.restskillsapi.domain.SkillNewDTO;
import com.ak.restskillsapi.domain.repository.SkillDTORepository;
import com.ak.restskillsapi.exception.DatabaseException;


@Repository
public class PostgreSQLSkillDTORepository implements SkillDTORepository {
	
	static final String url = "jdbc:postgresql://localhost:5432/restskillsdb";
	static final String user = "postgres";
	static final String password = "pass";
	
	private Connection connect() throws SQLException {	try {
		Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException e1) {
		System.out.println("Class not found " + e1);
		e1.printStackTrace();
	}
		return DriverManager.getConnection(url, user, password);
	}
	
	private void close(Connection connection, Statement statement) {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
		}
	}
	
	public void createTableSkills() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = connect();
			statement = connection.createStatement();
			String sqlCreate = "CREATE TABLE IF NOT EXISTS skills" 
			        + "  (id           		SERIAL PRIMARY KEY,"
			        + "   skillName         VARCHAR(40) UNIQUE NOT NULL)";
			statement.executeUpdate(sqlCreate);
		} catch  (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("creating table failed", e);
		} finally {
			close(connection, statement);
		}
	}

	@Override
	public List<SkillDTO> getAllSkills() {
		Connection connection = null;
		Statement statement = null;
		List<SkillDTO> listOfSkills = new ArrayList<SkillDTO>();
		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "SELECT * FROM skills";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				SkillDTO skillDTO = resultToSkillDTO(resultSet);
				listOfSkills.add(skillDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("getting all skills failed", e);
		} finally {
			close(connection, statement);
		}
		return listOfSkills;
	}

	private SkillDTO resultToSkillDTO(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String skillName = resultSet.getString("skillName");
		SkillDTO skillDTO = new SkillDTO();
		skillDTO.setId(id);
		skillDTO.setSkillName(skillName);
		return skillDTO;
	}

	@Override
	public void addNewSkill(SkillNewDTO skillNewDTO) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connect();
			String sql = "INSERT INTO skills (skillName) VALUES (?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1, skillNewDTO.getSkillName());
			
			statement.executeUpdate();
		}catch (SQLException  e) {
			e.printStackTrace();
			throw new DatabaseException("Adding new skill failed. This skill may already exist.", e);
		} finally {
			close(connection, statement);
		}
	}
	
	
}
