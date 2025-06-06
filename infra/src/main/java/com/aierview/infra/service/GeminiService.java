package com.aierview.infra.service;

import com.aierview.application.gateway.AI.IGenerateFeedbackGateway;
import com.aierview.application.gateway.AI.IGenerateQuestionGateway;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.entity.GenerateQuestionnaireParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.BusinessException;
import com.aierview.infra.helper.KafkaFeedbackProducer;
import com.aierview.infra.persistence.entity.AnswerEntity;
import com.aierview.infra.persistence.entity.QuestionEntity;
import com.aierview.infra.persistence.repository.AnswerRepository;
import com.aierview.infra.persistence.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GeminiService implements IGenerateQuestionGateway, IGenerateFeedbackGateway {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QuestionRepository questionRepository;


    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private KafkaFeedbackProducer kafkaFeedbackProducer;

    private String buildApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
    }

    //Generates question
    @Override
    public List<Question> generate(GenerateQuestionnaireParams params) {
        String prompt = buildQuestionPrompt(params);
        String response = getGeminiResponse(prompt);
        if (response == null || response.isEmpty()) return Collections.emptyList();
        List<String> stringQuestions = splitQuestions(response);
        return toQuestionsList(params, stringQuestions);
    }

    //Generates feedback
    @Override
    public void sendToAsyncGeneration(Answer answer) {
        kafkaFeedbackProducer.send(answer);
    }

    public void generateFeedbackAndUpdate(Answer answer) throws BusinessException {
        AnswerEntity answerEntity = answerRepository.findById(answer.getId())
                .orElseThrow(() -> new BusinessException("Could not find answer for question id " + answer.getQuestionId()));
        String prompt = buildFeedbackPrompt(answer);
        String feedback = getGeminiResponse(prompt);
        answerEntity.setFeedback(feedback);
        answerRepository.save(answerEntity);
    }

    private String getGeminiResponse(String prompt) {
        String apiUrl = buildApiUrl();
        HttpEntity<Map<String, Object>> requestEntity = buildHttpEntity(prompt);
        Map<String, Object> response = restTemplate.postForObject(apiUrl, requestEntity, Map.class);
        String responseText = extractTextFromResponse(response);
        return responseText;
    }

    private HttpEntity<Map<String, Object>> buildHttpEntity(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = buildQuestion(prompt);
        return new HttpEntity<>(requestBody, headers);
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

    private String buildQuestionPrompt(GenerateQuestionnaireParams params) {
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

    private String buildFeedbackPrompt(Answer answer) {
        QuestionEntity question = questionRepository.findById(answer.getQuestionId()).get();
        StringBuilder prompt = new StringBuilder();
        prompt
                .append("Você é um entrevistador técnico avaliando uma resposta de um candidato.")
                .append("\nA pergunta feita foi: ").append(question.getStatement())
                .append("\nAgora, avalie a resposta fornecida. Sua análise deve incluir:")
                .append("\n* Correção da resposta – está certa ou errada? Justifique.")
                .append("\n* Pontos fortes – aspectos positivos na formulação da resposta (ex: clareza, objetividade, contextualização).")
                .append("\nApresente sua avaliação de forma clara, objetiva e didática, " +
                        "como se estivesse orientando o candidato para melhorar sua performance " +
                        "tendo em conta que vaga é para o nível .").append(question.getLevel());
        return prompt.toString();
    }


    private List<Question> toQuestionsList(GenerateQuestionnaireParams params, List<String> stringsQuestions) {
        return stringsQuestions.stream()
                .filter(statement -> statement != null && !statement.trim().isEmpty())
                .map(statement -> Question.builder()
                        .framework(params.getFramework())
                        .language(params.getLanguage())
                        .specialty(params.getSpecialty())
                        .statement(statement)
                        .level(params.getLevel())
                        .build())
                .collect(Collectors.toList());
    }
}
