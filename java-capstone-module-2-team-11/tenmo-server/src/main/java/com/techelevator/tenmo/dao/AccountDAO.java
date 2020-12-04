package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {

	BigDecimal getUserBal(int userId);

	boolean verifyBalance(BigDecimal transferAmt, int userIdFrom);
	
	void executeSendTransaction(BigDecimal transferAmt, int userIdFrom, int userIdTo);
	
	
}
