package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class TransferSqlDAO implements TransferDAO{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
       
    }

	@Override
	public List<Transfer> usersTransfer(int userId) {
		
		List<Transfer> transfers = new ArrayList<Transfer>();
		
		String sqlGetUserTrans = "SELECT t.*, u.username AS userFrom, v.username AS userTo " + 
				"FROM transfers t " + 
				"JOIN accounts a ON t.account_from = a.account_id " + 
				"JOIN accounts b ON t.account_to = b.account_id " + 
				"JOIN users u ON a.user_id = u.user_id " + 
				"JOIN users v ON b.user_id = v.user_id " + 
				"WHERE a.user_id = ? OR b.user_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetUserTrans, userId, userId);
		
		while(results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		
		return transfers;
	}
	
	@Override
	public void transferRecord(int userFrom, int userTo, BigDecimal amount) {
		
		//Extracting the account_id for the from user
		String sqlAccountFrom = "SELECT account_id " +
							"FROM accounts " +
							"WHERE user_id = ?";
		
		SqlRowSet accountFromInt = jdbcTemplate.queryForRowSet(sqlAccountFrom, userFrom);
		
		accountFromInt.next();
			int accountFrom = accountFromInt.getInt("account_id");
		
			//Extracting the account_id for the to user
		String sqlAccountTo = "SELECT account_id " +
							"FROM accounts " +
							"WHERE user_id = ?";
		
		SqlRowSet accountToInt = jdbcTemplate.queryForRowSet(sqlAccountTo, userTo);

		accountToInt.next();
		int accountTo = accountToInt.getInt("account_id");
	
		
		//Inserting the transaction into the Transfer table with the two extracted account_id's
		String sqlInsertTransactionRecord = "INSERT into transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "+
											"VALUES (DEFAULT, 2, 2, ?, ?, ?)";
		
		jdbcTemplate.update(sqlInsertTransactionRecord, accountFrom, accountTo, amount);

		
	}
	

	public Transfer getTransfer(int transferId) {
		
		String sqlGetTrans = "SELECT t.*, u.username AS userFrom, v.username AS userTo " +
				"FROM transfers t " +
				"JOIN accounts a ON t.account_from = a.account_id " +
				"JOIN accounts b ON t.account_to = b.account_id " +
				"JOIN users u ON a.user_id = u.user_id " +
				"JOIN users v ON b.user_id = v.user_id " +
				"WHERE t.transfer_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTrans, transferId);
		results.next();
		Transfer returnMe = mapRowToTransfer(results);
		return returnMe;
		
	}
	
	private Transfer mapRowToTransfer(SqlRowSet rs) {
		Transfer transfer = new Transfer();
		transfer.setTransfer_id(rs.getLong("transfer_id"));
		transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
		transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
		transfer.setAccount_from(rs.getInt("account_from"));
		transfer.setAccount_to(rs.getInt("account_to"));
		transfer.setAmount(rs.getBigDecimal("amount"));
		transfer.setUserfrom(rs.getString("userfrom"));
		transfer.setUserto(rs.getString("userto"));
		
		return transfer;
	}
//	private Transfer mapRow(SqlRowSet rs) {
//		Transfer transfer = new Transfer();
//		transfer.setTransfer_id(rs.getLong("transfer_id"));
//		transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
//		transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
//		transfer.setAccount_from(rs.getInt("account_from"));
//		transfer.setAccount_to(rs.getInt("account_to"));
//		transfer.setAmount(rs.getBigDecimal("amount"));
//		return transfer;
//	}
	
//	String sql ="SELECT t.*, u.username AS userFrom, v.username AS userTo\n" + 
//			"FROM transfers t \n" + 
//			"JOIN accounts a ON t.account_from = a.account_id \n" + 
//			"JOIN accounts b ON t.account_to = b.account_id \n" + 
//			"JOIN users u ON a.user_id = u.user_id  \n" + 
//			"JOIN users v ON b.user_id = v.user_id \n" + 
//			"WHERE t.transfer_id = 4;";
	
	

}
