package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class AccountSqlDAO implements AccountDAO{
	

		
		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		public AccountSqlDAO() {
			
		}
		
		public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}
		@Override
		public BigDecimal getUserBal(int userId) {
			
			String sqlGetBal = "SELECT balance FROM accounts WHERE user_id = ?";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetBal, userId);
			results.next();
			BigDecimal bal = results.getBigDecimal(1);
			return bal;
		}
		
		@Override
		public boolean verifyBalance(BigDecimal transferAmt, int userIdFrom) {
			
			String sqlCheckBal = "SELECT balance FROM accounts WHERE user_id = ?";
			
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCheckBal, userIdFrom);
			results.next();
			BigDecimal bal = results.getBigDecimal("balance");
			
			int res = transferAmt.compareTo(bal);
			
			if (res == 1 ) {
				return false;
			}else {
				return true;
			}

		}
		
		@Override
		
		public void executeSendTransaction(BigDecimal transferAmt, int userIdFrom, int userIdTo) {
			
			String sqlUpdateBalFrom = "UPDATE accounts " +
								  "SET balance = (balance - ?) "+
								  "WHERE user_id = ?";
			
			jdbcTemplate.update(sqlUpdateBalFrom, transferAmt, userIdFrom);
			
			String sqlUpdateBalTo = "UPDATE accounts " +
					  "SET balance = (balance + ?) "+
					  "WHERE user_id = ?";

			jdbcTemplate.update(sqlUpdateBalTo, transferAmt, userIdTo);
			
		}

	

}
