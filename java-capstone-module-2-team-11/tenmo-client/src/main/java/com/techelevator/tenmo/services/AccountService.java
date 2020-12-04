package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;

public class AccountService {
	
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser currentUser;
	private final String INVALID_ACCOUNT_MSG = "Incorrect credentials for account. Please try again.";
	
	public AccountService(String url, AuthenticatedUser currentUser) {
		this.currentUser = currentUser;
		BASE_URL = url;
	}
	
	public BigDecimal getUserBal() throws AccountServiceException {
		 BigDecimal balance = new BigDecimal(0);
		
		 try {
		 
		 balance = restTemplate.exchange(BASE_URL + "balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		 
		 } catch (RestClientResponseException ex) {
				throw new AccountServiceException(INVALID_ACCOUNT_MSG);
			}
		 
		 System.out.println("Your current account balance is: $" + balance);
		 
		 return balance;
	}
	
	private HttpEntity makeAuthEntity() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(currentUser.getToken());
	    HttpEntity entity = new HttpEntity<>(headers);
	    return entity;
	}

}
