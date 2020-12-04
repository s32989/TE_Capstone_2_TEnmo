package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNotFoundException;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
	
	@Autowired
	private TransferDAO transferDAO;
	@Autowired
	private	UserDAO userDAO;
	@Autowired
	private AccountDAO accountDAO;
	
	public TransferController() {
		
	}
	
	public TransferController(TransferDAO transferDAO, UserDAO userDAO, AccountDAO accountDAO) {
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
		this.accountDAO = accountDAO;
	}
	
	@RequestMapping(path = "/account/transfers/{userId}", method = RequestMethod.GET)
	public List<Transfer> getTransfers(@PathVariable int userId) throws TransferNotFoundException {
		List<Transfer> getUs = transferDAO.usersTransfer(userId);
		return getUs;
	}
	
	@RequestMapping(path = "transfers/send", method = RequestMethod.POST)
	public String sendTransferRequest(@RequestBody Transfer transfer) {
		
		if(!accountDAO.verifyBalance(transfer.getAmount(), transfer.getUserIdFrom())) {		//verifyBalance returns a boolean: 
			return "Insufficient funds!";
		}else {
			accountDAO.executeSendTransaction(transfer.getAmount(),transfer.getUserIdFrom(),transfer.getUserIdTo());										
			transferDAO.transferRecord(transfer.getUserIdFrom(), transfer.getUserIdTo(), transfer.getAmount()); 													// this records the transfer in the transfers table
			return "Your funds have been transferred!";
				
		}

	}

	@RequestMapping(path = "account/transfers/user/{transferId}", method = RequestMethod.GET)
	public Transfer getTransfer(@PathVariable int transferId) throws TransferNotFoundException {
		Transfer getMe = transferDAO.getTransfer(transferId);
		return getMe;
	}
	

}
