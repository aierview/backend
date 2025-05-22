package com.aierview.infra.service.contract;

import com.aierview.domain.entity.GenerateQuestionParams;

import java.util.List;

public interface IGeminiService {
    List<String> getGeminiResponse(GenerateQuestionParams params);
}
