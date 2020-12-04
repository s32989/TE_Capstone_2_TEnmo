package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser currentUser;
	private final String INVALID_TRANSFER_MSG = "Incorrect credentials for transfer. Please try again.";
	
	public TransferService(String url, AuthenticatedUser currentUser) {
		this.currentUser = currentUser;
		BASE_URL = url;
	}
	
	public List<Integer> getTransferHistory() throws TransferServiceException {
		
		Scanner input = new Scanner(System.in);
		
		Transfer[] transfers = null;

		try {
				
		transfers = restTemplate.exchange(BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		
		} catch (RestClientResponseException ex) {
			throw new TransferServiceException(INVALID_TRANSFER_MSG);
		}
		
		List<Integer> transIds = new ArrayList<Integer>();
		
		if (transfers.length == 0) {
			System.out.println("No transfers on record");
			return transIds;
		}
		System.out.println("------------------------------------------");
		System.out.println("Transfers");
		System.out.println("ID \tFrom/To \tAmount");
		System.out.println("------------------------------------------");

		
		for (Transfer t : transfers) {
			transIds.add(t.getTransfer_id());
			
			
			//System.out.println("id: " + t.getTransfer_id() + " amount: $" + t.getAmount());
			if (t.getUserfrom().equals(currentUser.getUser().getUsername())) {
				
				
				System.out.println(t.getTransfer_id() + "\t" + t.getUserto() + " \t\t$" + t.getAmount());
			
			
			}
			
			if (t.getUserto().equals(currentUser.getUser().getUsername())) {
				System.out.println(t.getTransfer_id() + "\t" + t.getUserfrom() + "\t\t$" + t.getAmount());
			}

	
		}
		
		System.out.println("------------------------------------------");

		return transIds;
		
	}
	
	public void sendTEBucks() throws TransferServiceException {
		
		Scanner scanner = new Scanner(System.in);
		
		User[] users = null;
		
		try {
		
		users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		
		} catch (RestClientResponseException ex) {
			throw new TransferServiceException(INVALID_TRANSFER_MSG);
		}
		
		List<Integer> ids = new ArrayList<Integer>();
		
		for(User u : users) {
			ids.add(u.getId());
			System.out.println("ID: "+u.getId()+ " \tName: " + u.getUsername());
		}
		
		System.out.println("\nEnter ID of user you are sending to (0 to cancel): ");		
		
		int userIdTo = scanner.nextInt();
		
		if (userIdTo == 0) {
			
			return;
			
		} else {
		
			while(!ids.contains(userIdTo) || userIdTo == currentUser.getUser().getId()) {
			
				System.out.println("Please select a VALID user id to transfer to: ");
				userIdTo = scanner.nextInt();
			
			}
		
			System.out.println("Enter amount: ");
		
			BigDecimal amountToTransfer = scanner.nextBigDecimal();
		
			Transfer transferAttempt = new Transfer();								
			transferAttempt.setUserIdFrom(currentUser.getUser().getId());
			transferAttempt.setUserIdTo(userIdTo);
			transferAttempt.setAmount(amountToTransfer);

			String transferResult = restTemplate.exchange(BASE_URL + "transfers/send", HttpMethod.POST, makeTransferEntity(transferAttempt), String.class).getBody();
		
			System.out.println(transferResult);

			}
	}
	

	public String getSpecificTransfer(List<Integer> transIds) throws TransferServiceException {
		
		Transfer[] transfers = null;
		
		try {
		
			transfers = restTemplate.exchange(BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		
		} catch (RestClientResponseException ex) {
			throw new TransferServiceException(INVALID_TRANSFER_MSG);
		}
		Scanner input = new Scanner(System.in);
		System.out.println("\nWould you like details on a specific transfer? (Y/N)");
		String yesOrNo = input.nextLine();
		
		while (!yesOrNo.trim().equalsIgnoreCase("y") && !yesOrNo.trim().equalsIgnoreCase("n")) {
			System.out.println("Please enter (Y/N)");
			yesOrNo = input.nextLine();
		}
		
		if (yesOrNo.equalsIgnoreCase("n")) {
			
			return "";
			
			
		}else {
			
			System.out.println("\nPlease enter transfer ID to view details (0 to cancel):");		//Add function for pressing 0 to cancel out to main menu
			
			int transferId = input.nextInt();
			
			if (transferId == 0) {
				return "";
			} else {
			
				while(!transIds.contains(transferId)) {
				
					System.out.println("Please enter a valid ID");
					transferId = input.nextInt();
				
			}
			
			Transfer transfer = new Transfer();
			
			transfer = restTemplate.exchange(BASE_URL + "account/transfers/user/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
			
			System.out.println("------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("------------------------------------------");
			System.out.println("ID: " + transfer.getTransfer_id());
			System.out.println("From: " + transfer.getUserfrom());
			System.out.println("To: " + transfer.getUserto());
			if(transfer.getTransfer_type_id() == 1) {
				System.out.println("Type: Request");
			}
			if(transfer.getTransfer_type_id() == 2) {
				System.out.println("Type: Send");
			}
			if(transfer.getTransfer_status_id() == 1) {
				System.out.println("Status: Pending");
			}
			if(transfer.getTransfer_status_id() == 2) {
				System.out.println("Status: Approved");
			}
			if(transfer.getTransfer_status_id() == 3) {
				System.out.println("Status: Rejected");
			}
			System.out.println("Amount: $" + transfer.getAmount());
			System.out.println("------------------------------------------");

			
			return"";
			
			}
		}
		
	}
	

			
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setBearerAuth(currentUser.getToken());
		    HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		    return entity;
	}
	
	private HttpEntity makeAuthEntity() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(currentUser.getToken());
	    HttpEntity entity = new HttpEntity<>(headers);
	    return entity;
	}
	
//	private void showUsersAndPickOne() {
//		
//	}
	 
}
