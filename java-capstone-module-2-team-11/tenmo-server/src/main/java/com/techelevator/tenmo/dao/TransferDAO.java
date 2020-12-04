package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> usersTransfer(int userId);
	
	void transferRecord(int userFrom, int userTo, BigDecimal amount);
	
	Transfer getTransfer(int transferId);
}
