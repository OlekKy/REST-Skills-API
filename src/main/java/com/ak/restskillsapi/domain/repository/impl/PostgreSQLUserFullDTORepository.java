package com.ak.restskillsapi.domain.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.ak.restskillsapi.domain.SaveSkillsRequest;
import com.ak.restskillsapi.domain.UserCreatedDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.UserNewDTO;
import com.ak.restskillsapi.domain.repository.UserFullDTORepository;
import com.ak.restskillsapi.exception.DatabaseException;

@Repository
public class PostgreSQLUserFullDTORepository implements UserFullDTORepository {

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
	
	
	
	public void createTableUsers() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = connect();
			statement = connection.createStatement();
			String sqlCreate = "CREATE TABLE IF NOT EXISTS users" 
			        + "  (id            	VARCHAR(40) PRIMARY KEY ,"
			        + "   email		        VARCHAR(40) ,"
			        + "	  name				VARCHAR(40) ,"
			        + "	  password			VARCHAR(40) )";
			statement.executeUpdate(sqlCreate);
		} catch  (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("creating table failed", e);
		} finally {
			close(connection, statement);
		}
	}
	
	@Override
	public void createTableUserSkills() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = connect();
			statement = connection.createStatement();
			String sqlCreate = "CREATE TABLE IF NOT EXISTS userSkills" 
			        + "  (userId            VARCHAR(40) ,"
			        + "   skillId			INTEGER ,"
			        + "   FOREIGN KEY (userId)	REFERENCES users(id),"
			        + "   FOREIGN KEY (skillId) REFERENCES skills(id),"
			        + "   PRIMARY KEY (userId, skillId)	 )";
			statement.executeUpdate(sqlCreate);
		} catch  (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("creating table user skills failed", e);
		} finally {
			close(connection, statement);
		}
		
	}
	
	@Override
	public void createNewUser(UserNewDTO userNewDTO, String sessionId, String pass) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connect();
			String sql = "INSERT INTO users (id, email, name, password) VALUES (?,?,?,?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1, sessionId);			
			statement.setString(2, userNewDTO.getEmail());
			statement.setString(3, userNewDTO.getName());
			statement.setString(4, pass);
			statement.executeUpdate();
		}catch (SQLException  e) {
			e.printStackTrace();
			throw new DatabaseException("creating new user failed", e);
		} finally {
			close(connection, statement);
		}
		
	}

	@Override
	public UserCreatedDTO retrieveUser(String id) {
		Connection connection = null;
		PreparedStatement statement = null;
		UserCreatedDTO userCreatedDTO = null;
		try {
			connection = connect();
			String sql = "SELECT * from users where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userCreatedDTO = resultToUser(resultSet);
			}
			statement.close();
		} catch (SQLException e) {
			throw new DatabaseException("getting user info failed", e);
		} finally {
			close(connection, statement);
		}
		return userCreatedDTO;
	}
	
	private UserCreatedDTO resultToUser(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String email = resultSet.getString("email");
		String name = resultSet.getString("name");
		String password = resultSet.getString("password");
		UserCreatedDTO user = new UserCreatedDTO();
		user.setId(id);
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		return user;
	}

	@Override
	public UserFullDTO addSkillsToUser(SaveSkillsRequest saveSkillsRequest) {
		Connection connection = null;
		PreparedStatement statement = null;
		UserFullDTO userFullDTO = null;
		try {
			connection = connect(false);
			String sql = "INSERT INTO userSkills (userId, skillId) VALUES (?,?) ";
			statement = connection.prepareStatement(sql);
			for(Long skill : saveSkillsRequest.getSkillsIds()) {	
			statement.setString(1, saveSkillsRequest.getUserId());
			statement.setLong(2, skill);
			statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			throw new DatabaseException("update user's skills failed", e);
		} finally {
			close(connection, statement);
		}
		return userFullDTO;
	}

	

}
