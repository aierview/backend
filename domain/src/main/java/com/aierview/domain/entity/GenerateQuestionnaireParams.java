package com.aierview.domain.entity;

import com.aierview.domain.enums.QuestionLevel;
import com.aierview.domain.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestionnaireParams {
    private String framework;
    private String language;
    private Specialty specialty;
    private QuestionLevel level;
}