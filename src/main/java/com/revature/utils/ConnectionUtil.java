package com.revature.utils;

import java.sql.Connection;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	private static Connection conn = null;

	// Define a method to get the connection

	public static Connection getConnection() {

		try {

			// Check if a connection doesn't exist - if so then make one

			if (conn == null) {

				// 'hot-fix' to ensure that the driver loads correctly when our application starts

				Class.forName("org.postgresql.Driver"); 
				
				// more secure way to input our database credentials
				
				Properties props = new Properties();
				
				InputStream input = ConnectionUtil.class.getClassLoader().getResourceAsStream("connection.properties");
				
				props.load(input);
				
				String url = props.getProperty("url");
				String username = props.getProperty("username");
				String password = props.getProperty("password");

				conn = DriverManager.getConnection(url, username, password);
				
				return conn;
				
			} else {
				
				return conn;
			}

		} catch (SQLException | IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}

		return null;
	}
}
