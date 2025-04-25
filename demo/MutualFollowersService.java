package com.example.mutualfollowers.service;

import com.example.mutualfollowers.model.ResultResponse;
import com.example.mutualfollowers.model.User;
import com.example.mutualfollowers.model.UsersRequest;
import com.example.mutualfollowers.model.WebhookResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MutualFollowersService {

    private static final Logger logger = LoggerFactory.getLogger(MutualFollowersService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${generate.webhook.url}")
    private String generateWebhookUrl;

    @Value("${registration.number}")
    private String registrationNumber;

    public MutualFollowersService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            logger.info("Application started. Calling generate webhook endpoint...");
            
            // 1. Call the generateWebhook endpoint
            WebhookResponse webhookResponse = callGenerateWebhookEndpoint();
            logger.info("Received webhook response: {}", webhookResponse);
            
            // 2. Process the data to find mutual followers
            List<List<Integer>> mutualFollowPairs = findMutualFollowers(webhookResponse.getData().getUsers());
            logger.info("Found mutual followers: {}", mutualFollowPairs);
            
            // 3. Create the result response
            ResultResponse resultResponse = new ResultResponse(registrationNumber, mutualFollowPairs);
            
            // 4. Send the result to the webhook URL
            sendResultToWebhook(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), resultResponse);
            
        } catch (Exception e) {
            logger.error("Error during application startup processing", e);
        }
    }

    private WebhookResponse callGenerateWebhookEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                generateWebhookUrl,
                HttpMethod.POST,
                entity,
                WebhookResponse.class
        );
        
        return response.getBody();
    }

    private List<List<Integer>> findMutualFollowers(UsersRequest usersRequest) {
        List<User> users = usersRequest.getUsers();
        Set<String> processedPairs = new HashSet<>();
        List<List<Integer>> mutualFollowPairs = new ArrayList<>();
        
        // Create a map of userId to User for easy lookup
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
        
        // Check for mutual follows
        for (User user : users) {
            int userId = user.getId();
            
            for (Integer followId : user.getFollows()) {
                // Create a unique key for this pair (always min,max)
                int minId = Math.min(userId, followId);
                int maxId = Math.max(userId, followId);
                String pairKey = minId + "," + maxId;
                
                // Skip if we've already processed this pair
                if (processedPairs.contains(pairKey)) {
                    continue;
                }
                
                // Mark as processed
                processedPairs.add(pairKey);
                
                // Check if the follow is mutual
                User followUser = userMap.get(followId);
                if (followUser != null && followUser.getFollows().contains(userId)) {
                    // Add the mutual follow pair [min, max]
                    List<Integer> pair = Arrays.asList(minId, maxId);
                    mutualFollowPairs.add(pair);
                }
            }
        }
        
        return mutualFollowPairs;
    }

    private void sendResultToWebhook(String webhookUrl, String accessToken, ResultResponse resultResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        HttpEntity<ResultResponse> entity = new HttpEntity<>(resultResponse, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        
        logger.info("Webhook response status: {}", response.getStatusCode());
        logger.info("Webhook response body: {}", response.getBody());
    }
}