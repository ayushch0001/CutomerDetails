package com.assement.CusotmerApplication.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assement.CusotmerApplication.entity.TokenRequest;
import com.assement.CusotmerApplication.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenService {

	private static TokenRequest token;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	public TokenService() {
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
	}

	
	
	public TokenRequest getToken(User user) {
		String tokenUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

		try {
			String jsonResponse = restTemplate.postForObject(tokenUrl, requestEntity, String.class);

			 token = objectMapper.readValue(jsonResponse, TokenRequest.class);
			System.out.println(token.toString());
		} catch (Exception e) {
			System.out.println(e);
			
		}
		
		return token;
	}
	
	
}
