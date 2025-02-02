package com.perisatto.fiapprj.authentication_service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CognitoAuthLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
		RequestBodyDTO request = null;
		String clientId = System.getenv("AWS_COGNITO_CLIENT_ID");
		String clientSecret = System.getenv("AWS_COGNITO_CLIENT_SECRET");

		try {    	
			request = objectMapper.readValue(input.getBody(), RequestBodyDTO.class);			
		} catch (Exception e) {
			context.getLogger().log("Erro ao deserializar o corpo: " + e.getMessage());
			return createErrorResponse(400, "Erro ao deserializar o corpo: " + e.getMessage()); 
		}
		
		String email = request.getEmail();
		String documentNumber = request.getDocumentNumber();
		String secretHash = calculateSecretHash(clientId, clientSecret, email);
		
		AWSCognitoIdentityProvider cognitoClient = AWSCognitoIdentityProviderClientBuilder.defaultClient();

		InitiateAuthRequest authRequest = new InitiateAuthRequest()
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.withClientId(clientId)
				.addAuthParametersEntry("USERNAME", email)
				.addAuthParametersEntry("PASSWORD", documentNumber)
				.addAuthParametersEntry("SECRET_HASH", secretHash);
		
		try {
			InitiateAuthResult authResult = cognitoClient.initiateAuth(authRequest);
			String token = authResult.getAuthenticationResult().getIdToken();			
			return createErrorResponse(200, "{\"accessToken\": \""+ token + "\" }");
		} catch (Exception e) {
			context.getLogger().log("Error during authentication: " + e.getMessage());
			return createErrorResponse(400, "Error during authentication: " + e.getMessage());
		}

	}

	private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-type", "application/json");
		headers.put("X-Content-Header", "application/json");

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
				.withHeaders(headers);
		response.setStatusCode(statusCode);
		response.setBody(message);
		return response;
	}
	
	public static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
	    final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	    
	    SecretKeySpec signingKey = new SecretKeySpec(
	            userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
	            HMAC_SHA256_ALGORITHM);
	    try {
	        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
	        mac.init(signingKey);
	        mac.update(userName.getBytes(StandardCharsets.UTF_8));
	        byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(rawHmac);
	    } catch (Exception e) {
	        throw new RuntimeException("Error while calculating ");
	    }
	}
}