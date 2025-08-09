package com.aierview.backend.interview.infra.adapter.gemini;

import com.aierview.backend.interview.domain.contract.IA.IGenerateQuestions;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.shared.utils.GeminiFunctUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiServiceAdapter implements IGenerateQuestions {
    private final GeminiFunctUtils geminiFunctUtils;

    @Override
    public List<Question> execute(BeginInterviewRequest request, Long interviewId) {
        try {
            String prompt = this.geminiFunctUtils.generateQuestionsPrompt(request);
            List<String> questionsText = this.geminiFunctUtils.getResponse(prompt);
            Interview interviewRef = Interview.builder().id(interviewId).build();
            return questionsText.stream()
                    .filter(q -> !q.isEmpty())
                    .map(q -> Question
                            .builder().question(q).interview(interviewRef).build())
                    .collect(Collectors.toList());
        }catch (Exception e) {
            //log strategy
            throw new UnavailableIAServiceException();
        }
    }
}
