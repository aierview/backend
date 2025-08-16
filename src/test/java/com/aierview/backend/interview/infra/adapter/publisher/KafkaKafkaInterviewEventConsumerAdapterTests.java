package com.aierview.backend.interview.infra.adapter.publisher;

import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventConsumer;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KafkaKafkaInterviewEventConsumerAdapterTests {
    private IInterviewEventConsumer kafkaInterviewEventConsumer;
    private ISendCurrentQuestion sendCurrentQuestion;

    @BeforeEach
    void setUp() {
        this.sendCurrentQuestion = Mockito.mock(ISendCurrentQuestion.class);
        this.kafkaInterviewEventConsumer = new KafkaInterviewEventConsumerAdapter(sendCurrentQuestion);
    }


    @Test
    @DisplayName("should consume topic of kafka and send question")
    void shouldConsumeTopicOfKafkaAndSendQuestion() {
        CurrentQuestion currentQuestion = InterviewTestFixture.anyCurrentQuestion();
        Mockito.doNothing().when(this.sendCurrentQuestion).execute(currentQuestion);
        this.kafkaInterviewEventConsumer.consume(currentQuestion);
        Mockito.verify(this.sendCurrentQuestion, Mockito.times(1)).execute(currentQuestion);

    }
}
