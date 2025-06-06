package com.aierview.infra.helper;

import com.aierview.domain.entity.Answer;
import com.aierview.infra.service.GeminiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaFeedbackConsumer {
    private final GeminiService geminiService;


    @KafkaListener(
            topics = "generate-feedback-topic",
            groupId = "feedback-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(Answer answer, Acknowledgment ack) {
        try {
            geminiService.generateFeedbackAndUpdate(answer);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while processing message: " + e.getMessage(), e);
        }
    }
}
