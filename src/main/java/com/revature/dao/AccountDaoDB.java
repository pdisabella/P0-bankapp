package com.revature.dao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {
	private Connection conn = ConnectionUtil.getConnection();

	public Account addAccount(Account a) {

		String sql = "insert into accounts values (default, ?, ?, ?, ?);";

		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getOwnerId());
			ps.setDouble(2, a.getBalance());
			ps.setString(3, a.getType());
			ps.setBoolean(4, true);

			boolean success = ps.execute();

			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> getAccounts() {
		List<Account> accts = new ArrayList<Account>();

		String sql = "select * from accounts;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("acc_num"));
				a.setBalance(rs.getDouble("balance"));
				a.setType(rs.getString("acc_type"));
				a.setApproved(rs.getBoolean("is_pending"));
				a.setOwnerId(rs.getInt("u_id"));
				accts.add(a);
			}

			

			return accts;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public List<Account> getAccountsByUser(User u) {
		List<Account> accts = new ArrayList<Account>();

		String sql = "select * from accounts where u_id = ?;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Account a = new Account();
				a.setAccountNum(rs.getInt("acc_num"));
				a.setAccountBal(rs.getFloat("balance"));
				a.setAccountType(rs.getString("acc_type"));
				a.setPendingAccount(rs.getBoolean("is_pending"));
				a.setUserId(rs.getInt("u_id"));
				accts.add(a);
			}

			return accts;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public Account updateAccount(Account a) {
		String sql = "update accounts set is_pending = false where acc_num = ?;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1,id);

			boolean success = ps.execute();
			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public boolean removeAccount(Account a) {
		String sql = "delete from accounts where acc_num = ?;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, a.getId());

			boolean success = ps.execute();
			return success;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

}
