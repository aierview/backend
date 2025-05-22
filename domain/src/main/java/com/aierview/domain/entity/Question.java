package com.aierview.domain.entity;

import com.aierview.domain.enums.QuestionLevel;
import com.aierview.domain.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String id;
    private String framework;
    private String language;
    private Specialty specialty;
    private String statement;
    private QuestionLevel level;
}