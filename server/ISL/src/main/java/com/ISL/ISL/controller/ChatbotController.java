package com.ISL.ISL.controller;

import com.ISL.ISL.service.GeminiService;
import com.ISL.ISL.service.ISLModelService;

import com.ISL.ISL.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private ISLModelService islModelService;



    // 🟢 Case 1: ISL to AI Bot
    @PostMapping("/isl-to-ai")
    public String islToAiBot(@RequestParam String targetLanguage) {
        // 1️⃣ Hardcoded input in Hindi/Marathi
        String inputText = "कैमरा आधारित ISL से टेक्स्ट रूपांतरण कैसे काम करता है?";

        // 2️⃣ Translate input to English
        String translatedInput = translationService.translateText(inputText, "en");

        // 3️⃣ Get AI response from Gemini
        String aiResponse = geminiService.getGeminiResponse(translatedInput);

        // 4️⃣ Translate AI response back to the original language
        return translationService.translateText(aiResponse, targetLanguage);
    }


    // 🔵 Case 2: Text/Speech to AI Bot
    @PostMapping("/text-to-ai")
    public ResponseEntity<String> textToAiBot(@RequestBody Map<String, String> requestBody) {
        String inputText = requestBody.get("text"); // Extract user input
        if (inputText == null || inputText.isEmpty()) {
            return ResponseEntity.badRequest().body("Input text cannot be empty");
        }

        // Detect input language
        String detectedLang = translationService.detectLanguage(inputText);

        // Get AI response from Gemini
        String aiResponse = geminiService.getGeminiResponse(inputText);

        // Translate AI response back to input language if necessary
        String translatedResponse = translationService.translateText2(aiResponse, detectedLang);

        return ResponseEntity.ok(translatedResponse);
    }
}
