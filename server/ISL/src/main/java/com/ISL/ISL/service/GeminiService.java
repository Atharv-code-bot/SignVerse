package com.ISL.ISL.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getGeminiResponse(String userInput) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + geminiApiKey;

        // **Formatted Prompt for ISL Context**
        String formattedPrompt = "You are an AI assistant specialized in Indian Sign Language (ISL), you help deaf and dumb individuals by answering their queries "
                + "The following is your question: \"" + userInput + "\". "
                + "*You are strictly adviced to answer their queries in max 5 lines and  Respond in the same language as the input.";


        // Prepare request payload with maxTokens limit
        String requestBody = "{"
                + "\"contents\": [{ \"parts\": [{ \"text\": \"" + formattedPrompt.replace("\"", "\\\"") + "\" }] }]"
                + "}";


        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Make API call
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // Extract and return only the AI-generated text
        return extractTextFromResponse(response.getBody());
    }

    private String extractTextFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            if (textNode.isMissingNode()) {
                return "No valid response received.";
            }

            // Preserve text formatting (like **bold**)
            String extractedText = textNode.asText().trim();

            // Ensure the response ends at a sentence boundary
            int lastPeriodIndex = extractedText.lastIndexOf('.');
            if (lastPeriodIndex != -1) {
                extractedText = extractedText.substring(0, lastPeriodIndex + 1);  // Include the period
            }

            return extractedText;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error extracting AI response.";
        }
    }




}
