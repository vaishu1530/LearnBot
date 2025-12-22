package com.example.learnbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class StudyService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getStudyLogic(String userQuestion) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "LearnBot is offline: Missing API Key.";
        }

        String url = apiUrl + "?key=" + apiKey;

        // We add specific instructions to avoid 'Syntax Error' in Mermaid
        String systemPrompt = "You are 'LearnBot Pro'. Answer only academic questions.\n" +
            "1. Use Numbered Lists (1., 2.) for headings.\n" +
            "2. Use Bullet Points (•) for details.\n" +
            "3. DIAGRAM RULE: Use 'graph TD'. DO NOT use backticks (```) or the word 'mermaid' in the diagram code. Start the diagram immediately with 'graph TD'.\n" +
            "4. Put technical terms in parentheses ().";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nQuestion: " + userQuestion))
            ))
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            // This is the error message you asked for
            return "LearnBot is currently offline. Error: " + e.getMessage() + ". Please check your database connection and API key.";
        }
    }
}