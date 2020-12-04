package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
	
	private long transfer_id;
	private int account_id;
	private int transfer_type_id;
	private int transfer_status_id;
	private int account_from;
	private int account_to;
	private BigDecimal amount;

	private int userIdFrom;
	private int userIdTo;
	
	private String userfrom;
	private String userto;
	
	public String getUserfrom() {
		return userfrom;
	}

	public void setUserfrom(String userfrom) {
		this.userfrom = userfrom;
	}

	public String getUserto() {
		return userto;
	}

	public void setUserto(String userto) {
		this.userto = userto;
	}


	
	public int getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(int userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

	public int getUserIdTo() {
		return userIdTo;
	}

	public void setUserIdTo(int userIdTo) {
		this.userIdTo = userIdTo;
	}


	
	public Transfer() {
		
	}
	
	public Transfer(long transfer_id, int account_id, int transfer_type_id, int transfer_status_id, int account_from, int account_to,
			BigDecimal amount) {
		this.transfer_id = transfer_id;
		this.account_id = account_id;
		this.transfer_type_id = transfer_type_id;
		this.transfer_status_id = transfer_status_id;
		this.account_from = account_from;
		this.account_to = account_to;
		this.amount = amount;
	}

	public long getTransfer_id() {
		return transfer_id;
	}

	public void setTransfer_id(long transfer_id) {
		this.transfer_id = transfer_id;
	}

	public int getTransfer_type_id() {
		return transfer_type_id;
	}

	public void setTransfer_type_id(int transfer_type_id) {
		this.transfer_type_id = transfer_type_id;
	}

	public int getTransfer_status_id() {
		return transfer_status_id;
	}

	public void setTransfer_status_id(int transfer_status_id) {
		this.transfer_status_id = transfer_status_id;
	}

	public int getAccount_from() {
		return account_from;
	}

	public void setAccount_from(int account_from) {
		this.account_from = account_from;
	}

	public int getAccount_to() {
		return account_to;
	}

	public void setAccount_to(int account_to) {
		this.account_to = account_to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}


	

}
