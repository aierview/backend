package com.aierview.infra.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.infra.service.contract.IGeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeminiService implements IGeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private RestTemplate restTemplate;

    private String buildApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
    }

    @Override
    public List<String> getGeminiResponse(GenerateQuestionParams params) {
        String apiUrl = buildApiUrl();
        HttpEntity<Map<String, Object>> requestEntity = buildHttpEntity(params);

        Map<String, Object> response = restTemplate.postForObject(apiUrl, requestEntity, Map.class);
        String responseText = extractTextFromResponse(response);

        if (StringUtil.isNullOrEmpty(responseText)) return Collections.emptyList();

        return splitQuestions(responseText);
    }

    private HttpEntity<Map<String, Object>> buildHttpEntity(GenerateQuestionParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = buildPrompt(params);
        Map<String, Object> requestBody = buildQuestion(prompt);
        return new HttpEntity<>(requestBody, headers);
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("candidates")) return "";

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates.isEmpty()) return "";

        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

        if (parts == null || parts.isEmpty()) return "";

        return (String) parts.get(0).get("text");
    }

    private List<String> splitQuestions(String responseText) {
        return Arrays.stream(responseText.split(";"))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildQuestion(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);
        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{part});
        requestBody.put("contents", new Object[]{content});
        return requestBody;
    }

    private String buildPrompt(GenerateQuestionParams params) {
        StringBuilder prompt = new StringBuilder();
        prompt
                .append("Cenário: Simulação de uma entrevista técnica para a vaga de Desenvolvedor ")
                .append(params.getSpecialty()).append(" ")
                .append(params.getLanguage()).append(" ")
                .append(params.getLevel()).append("\n")
                .append("Framework: ").append(params.getFramework())
                .append("Objetivo: Gerar 10 questões técnicas para praticar a entrevista e separe cada pergunta por ';' no final.")
                .append("Instruções: Traga apenas as questões (sem respostas), sem nenhum texto extra.");
        return prompt.toString();
    }
}
