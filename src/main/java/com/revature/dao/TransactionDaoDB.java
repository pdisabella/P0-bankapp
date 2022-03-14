package com.revature.dao;

import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

import com.revature.beans.Transaction;
import com.revature.utils.ConnectionUtil;
import com.revature.beans.Account;
import com.revature.beans.User;

public class TransactionDaoDB implements TransactionDao {
	private Connection conn = ConnectionUtil.getConnection();
	public List<Transaction> getAllTransactions() {
		List<Transaction> allTransactions = new ArrayList<Transaction>();

		String sql = "select * from transactions;";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Transaction t = new Transaction();

				t.setId(rs.getInt("t_id"));
				t.setUserId(rs.getInt("u_id"));
				t.setAccountNum(rs.getInt("acc_num"));
				t.settAmount(rs.getFloat("t_amount"));
				t.settType(rs.getString("t_name"));

				allTransactions.add(t);
			}

			for (Transaction t : allTransactions) {

				System.out.println(t);
			}
			return allTransactions;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		return null;
	}
	}


