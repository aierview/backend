package com.aierview.application.usecase.contract;

import com.aierview.domain.entity.Question;

import java.util.List;

public interface ISaveQuestions {
    List<Question> save(List<Question> questions);
}
