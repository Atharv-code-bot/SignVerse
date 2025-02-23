package com.ISL.ISL.service;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private final Translate translate;

    // Constructor-based dependency injection
    public TranslationService(@Value("${google.api.key}") String apiKey) {
        this.translate = TranslateOptions.newBuilder()
                .setApiKey(apiKey)
                .build()
                .getService();
    }

    /**
     * Translates text to the target language, detecting source language automatically.
     */
    public String translateText(String text, String targetLang) {
        // Detect the source language
        Detection detection = translate.detect(text);
        String sourceLang = detection.getLanguage();

        // If already in English and target is English, return as is
        if ("en".equals(sourceLang) && "en".equals(targetLang)) {
            return text;
        }

        // Translate only if necessary
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        );

        return translation.getTranslatedText();
    }

    public String detectLanguage(String text) {
        Detection detection = translate.detect(text);
        return detection.getLanguage();
    }

    // Method to translate text to a target language
    public String translateText2(String text, String targetLang) {
        Detection detection = translate.detect(text);
        String sourceLang = detection.getLanguage();

        // If already in the target language, return as is
        if (sourceLang.equals(targetLang)) {
            return text;
        }

        // Translate if necessary
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        );

        return translation.getTranslatedText();
    }
}
