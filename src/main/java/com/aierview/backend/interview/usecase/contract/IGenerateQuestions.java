package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

import java.util.List;

public interface IGenerateQuestions {
    List<Question> execute(BeginInterviewRequest request, Long interviewId);
}
