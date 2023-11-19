package com.assement.CusotmerApplication.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.assement.CusotmerApplication.entity.Customer;
import com.assement.CusotmerApplication.entity.TokenRequest;
import com.assement.CusotmerApplication.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {

	private static ArrayList<HashMap<String, String>> listOfCustomer;
	private static TokenRequest token;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	private static User user;
	private static String createUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	
	
	
	@Autowired
	TokenService tokenService;

	public CustomerService() {
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
	}

	public void getToken(User user) {
		token = tokenService.getToken(user);
	}
	

	public void createCustomer(Customer customer) 
	{
		getToken(user);
		String tokenAuthorization = "Bearer " + token.getAccess_token();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", tokenAuthorization);
		HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrl).queryParam("cmd", "create");

		String finalUrl = builder.toUriString();

		try {
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(finalUrl, requestEntity, String.class);

			// Check the HTTP status code
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				String jsonResponse = responseEntity.getBody();

				System.out.println("Response: " + jsonResponse);
			} else {
				System.out.println("Error: " + responseEntity.getStatusCode());

			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());

		}
	}

	public ArrayList listOfCustomer() {

		getToken(user);
		String tokenAuthorization = "Bearer " + token.getAccess_token();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", tokenAuthorization);

		HttpEntity<?> requestEntity = new HttpEntity(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrl).queryParam("cmd",
				"get_customer_list");

		String finalUrl = builder.toUriString();

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity,
					String.class);

			// Check the HTTP status code
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				String jsonResponse = responseEntity.getBody();

				listOfCustomer = objectMapper.readValue(jsonResponse, ArrayList.class);

				for (HashMap c : listOfCustomer) {
					System.out.println(c.toString());
				}

			} else {
				System.out.println("Error: " + responseEntity.getStatusCode());
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		return listOfCustomer;
	}

	public void updateCustomer(String uuid,Customer customer) {

		//getToken(user);
		String tokenAuthorization = "Bearer " + token.getAccess_token();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", tokenAuthorization);

//		Customer customer = new Customer("Ayush", "Doe", "Elvnu Street", "H no 2", "Mumbai", "Delhi", "sam@gmail.com",
//				"12345678");

		HttpEntity<?> requestEntity = new HttpEntity(customer, headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrl).queryParam("cmd", "update")
				.queryParam("uuid", uuid);

		String finalUrl = builder.toUriString();

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.POST, requestEntity,
					String.class);

			// Check the HTTP status code
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				String jsonResponse = responseEntity.getBody();
				HashMap<String, String> temp = objectMapper.readValue(jsonResponse, HashMap.class);
				System.out.println("Response: " + temp.toString());

			} else {
				System.out.println("Error: " + responseEntity.getStatusCode());

			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());

		}
	}

	public void deleteCustomer(String uuid) {

		getToken(user);
		String tokenAuthorization = "Bearer " + token.getAccess_token();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", tokenAuthorization);
		HttpEntity<?> requestEntity = new HttpEntity(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrl).queryParam("cmd", "delete")
				.queryParam("uuid", uuid);

		String finalUrl = builder.toUriString();

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.POST, requestEntity,
					String.class);

			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				String jsonResponse = responseEntity.getBody();
				System.out.println("Response: " + jsonResponse.toString());

			} else {
				System.out.println("Error: " + responseEntity.getStatusCode());
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}

}
