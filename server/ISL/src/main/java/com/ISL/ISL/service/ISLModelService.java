package com.ISL.ISL.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;

@Service
public class ISLModelService {

    private static final Logger logger = LoggerFactory.getLogger(ISLModelService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ML_MODEL_ISL_URL = "http://127.0.0.1:5000/upload";
    // Convert ISL (video) to Text
    public String convertISLToText(MultipartFile videoFile) throws IOException {
        // Log the file being sent
        System.out.println("Sending video file: " + videoFile.getOriginalFilename());

        // Set headers for multipart request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the request body as a MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", new ByteArrayResource(videoFile.getBytes()) {
            @Override
            public String getFilename() {
                return videoFile.getOriginalFilename(); // Keep the original filename
            }
        });

        // Create the HTTP entity with headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the request
        ResponseEntity<String> response = restTemplate.exchange(
                ML_MODEL_ISL_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Log and return response
        System.out.println("Received response from ML model: " + response.getBody());
        return response.getBody();
    }
}
