package com.revature.dao;
import java.util.List;
import com.revature.beans.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.revature.utils.ConnectionUtil;


/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao {

	public static Connection conn = ConnectionUtil.getConnection();

	public User addUser(User user) {
		String sql = "insert into users values (default, ?, ?, ?, ?, ?);";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setBoolean(5, user.getUserType());

			boolean success = ps.execute();

			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public User getUser(Integer userId) {
		String sql = "select * from users where u_id = ?";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				User user = new User();
				user.setId(rs.getInt("u_id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("f_name"));
				user.setLastName(rs.getString("l_name"));
				user.setEmployeeAcct(rs.getBoolean("is_employee"));
				return user;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public User getUser(String username, String pass) {
		String sql = "select * from users where username = ?";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				User user = new User();
				user.setId(rs.getInt("u_id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("f_name"));
				user.setLastName(rs.getString("l_name"));
				user.setEmployeeAcct(rs.getBoolean("is_employee"));
				return user;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;

	}
		

	public List<User> getAllUsers() {
		List<User> allUsers = new ArrayList<User>();

		String sql = "select * from users;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt("u_id"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setFirstName(rs.getString("f_name"));
				u.setLastName(rs.getString("l_name"));
				u.setEmployeeAcct(rs.getBoolean("is_employee"));
				allUsers.add(u);
			}

			System.out.println("All Users:");
			System.out.println("\n");

			for (User u : allUsers) {

				System.out.print("User ID: ");
				System.out.print(u.getId());
				System.out.println("\n");
				System.out.print("Username: ");
				System.out.print(u.getUsername());
				System.out.println("\n");
				System.out.print("Password: ");
				System.out.print(u.getPassword());
				System.out.println("\n");
				System.out.print("First Name: ");
				System.out.print(u.getFirstName());
				System.out.println("\n");
				System.out.print("Last Name: ");
				System.out.print(u.getLastName());
				System.out.println("\n");
				System.out.print("Is Employee: ");
				System.out.print(u.isEmployeeAcct());
				System.out.println("\n");
			}

			return allUsers;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public User updateUser(User u) {
		String sql = "update users set username = ?, password = ?, f_name = ?, "
				+ "l_name = ?, is_employee = ?, where u_id = ?;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getFirstName());
			ps.setString(4, u.getLastName());
			ps.setBoolean(5, u.isEmployeeAcct());
			ps.setInt(6, u.getId());

			boolean success = ps.execute();
			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public boolean removeUser(User u) {
		String sql = "delete from users where u_id = ?;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, u.getId());

			boolean success = ps.execute();
			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

}
