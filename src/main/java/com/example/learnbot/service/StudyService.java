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
        String url = apiUrl + apiKey;

        // ADVANCED ACADEMIC SYSTEM PROMPT
        String systemPrompt = "You are 'LearnBot', a world-class Academic AI. " +
            "Your expertise includes: \n" +
            "1. ENGINEERING: Mechanical, Civil, Electrical, Electronics, Software, and Robotics.\n" +
            "2. COMPUTER SCIENCE: Coding (Java, Python, C, C++, etc.), Data Structures, Algorithms, Networking, IoT, complier Design, DBMS, SQl, HTMl,PHP, CSS, TYPescript, react, spring boot,  Cloud Computing, and Big Data.\n" +
            "3. CORE STUDIES: Mathematics (all levels), Physics, Chemistry, Biology, and History.\n" +
            "4.  agricluter and medical concept.\n" +
            "\n" +
            "You are 'LearnBot Pro'. You explain academic concepts systematically. " +
            "1. USE NUMBERS (1., 2., 3.) for main headings and layers.\n" +
            "2. USE BULLETS (•) for descriptions and details. NEVER use '*' symbols.\n" +
            "3. LEAVE LARGE GAPS between sections for readability.\n" +
            "4. PUT ALL PROTOCOLS AND TECHNICAL TERMS IN PARENTHESES () for faint styling.\n" +
            "5. EXPLAIN STEP-BY-STEP. Include a 'Diagram Description' for visual topics."+
            "6.  DIAGRAM SECTION: End with 'DIAGRAM: [Description of the visual]'.\n" +
            
            "INSTRUCTIONS:\n" +
            "-\"You are 'LearnBot Pro'. Answer academic questions (Engineering, IoT, Big Data, Science, etc.). \" +\r\n"
            + "            \"1. DO NOT use hashtags (#). Use bold numbers for main sections.\\n\" +\r\n"
            + "            \"2. Use bullet points (•) for details.\\n\" +\r\n"
            + "            \"3. For the OSI model or lists, list them 1 to 7 first, then explain each with a large gap.\\n\" +\r\n"
            + "            \"4. Put technical details in parentheses () so the UI can make them faint.\\n\" +\r\n"
            + "            \"5. Provide a simple example and a description of a diagram for every answer." +
            "               (A clear description of a visual diagram for this concept)\n" +
            "\n" +
           
            
        "RESPONSE RULES: " +
        "1. Use CLEAR HEADINGS for each section. " +
        "2. For lists (like the OSI model), list them first, then explain one-by-one with gaps. " +
        "3. DO NOT use long paragraphs. Use bullet points. " +
        "4. Language: Use a mix of simple explanations for concepts and technical terms for advanced levels. " +
        "5. If a topic is NOT academic, say 'I only help with studies.' " +
        "6. Always include a section called '### DIAGRAM DESCRIPTION'." +
        "7. Only refuse non-study questions (e.g. 'tell me a joke', 'who is this actor').";
        
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", systemPrompt + "\n\nUser Question: " + userQuestion))
            ))
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            return "LearnBot is currently offline. Please check your API Key or Connection.";
        }
    }
}