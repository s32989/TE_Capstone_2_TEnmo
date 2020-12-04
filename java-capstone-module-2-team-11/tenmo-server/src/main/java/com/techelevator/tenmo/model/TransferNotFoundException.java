package com.techelevator.tenmo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND, reason = "Transfer history not found.")
public class TransferNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public TransferNotFoundException() {
        super("Transfer history not found.");
    }
	
}
