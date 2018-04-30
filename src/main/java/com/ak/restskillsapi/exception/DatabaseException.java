package com.ak.restskillsapi.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
	
	private static final long serialVersionUID = -6581816302334116497L;
	public DatabaseException(String message, SQLException e) {
		super(message,e);
	}
	public DatabaseException(String message) {
		super(message);
	}
	public DatabaseException(SQLException e) {
		super(e);
	}

}
