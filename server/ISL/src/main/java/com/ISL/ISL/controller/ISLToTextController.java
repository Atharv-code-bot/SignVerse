package com.ISL.ISL.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ISL.ISL.service.ISLModelService;
import com.ISL.ISL.service.TranslationService;
import java.io.IOException;

@RestController
@RequestMapping("/isl")
public class ISLToTextController {

    private static final Logger logger = LoggerFactory.getLogger(ISLToTextController.class);
    private final ISLModelService islModelService;
    private final TranslationService translationService;

    public ISLToTextController(ISLModelService islModelService, TranslationService translationService) {
        this.islModelService = islModelService;
        this.translationService = translationService;
    }

    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertISLToText(
            @RequestParam("file") MultipartFile videoFile,
            @RequestParam("targetLang") String targetLang) { // ✅ Accept as form-data
        try {
            logger.info("Received file: {} for target language: {}", videoFile.getOriginalFilename(), targetLang);

            // Pass video file to ML model and get the ISL-to-text conversion
            String detectedText = islModelService.convertISLToText(videoFile);
            logger.info("Detected text: {}", detectedText);

            // Translate detected text to target language
            String translatedText = translationService.translateText(detectedText, targetLang);
            logger.info("Translated text: {}", translatedText);

            return ResponseEntity.ok("Final Translated Text: " + translatedText);
        } catch (IOException e) {
            logger.error("Error processing file: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

}
