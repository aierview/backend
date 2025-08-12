package com.aierview.backend.shared.utils;

import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GeminiFunctUtils {

    private final RestTemplate restTemplate;
    @Value("${gemini.api-key}")
    private String apiKey;
    @Value("${gemini.api-url}")
    private String apiUrl;

    public String generateQuestionsPrompt(BeginInterviewRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("📋 Contexto:\n");
        prompt.append("Você está atuando como entrevistador e irá conduzir uma entrevista técnica com um desenvolvedor ");
        prompt.append(request.getRole() + " de nível " + request.getInterviewLevel() + " em " + request.getStack() + ".\n\n");
        prompt.append("🎯 Objetivo:\n");
        prompt.append("Gerar 3 perguntas técnicas para que o entrevistado possa responder com base em seus conhecimentos e experiência.\n\n");
        prompt.append("📝 Instrução:\n");
        prompt.append("Liste as perguntas no seguinte formato:\n\n");
        prompt.append("pergunta\n");
        prompt.append("Uma pergunta por linha.\n");
        prompt.append("Adicione ## ao final de cada pergunta para indicar o término da pergunta.\n");
        return prompt.toString();
    }

    public List<String> getResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt.toString());
        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{part});
        requestBody.put("contents", new Object[]{content});
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        URI uri = UriComponentsBuilder.fromHttpUrl(this.apiUrl).queryParam("key", this.apiKey).build().toUri();
        Map<String, Object> response = restTemplate.postForObject(uri, requestEntity, Map.class);
        if (response == null || !response.containsKey("candidates"))
            throw new UnavailableIAServiceException();

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates.isEmpty()) throw new UnavailableIAServiceException();

        content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) throw new UnavailableIAServiceException();
        List<String> responseText = Arrays.stream(((String) parts.get(0).get("text")).split("##"))
                .map(String::trim)
                .collect(Collectors.toList());
        return responseText;
    }
}
