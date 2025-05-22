package com.aierview.infra.persistence.entity;

import com.aierview.domain.enums.QuestionLevel;
import com.aierview.domain.enums.Specialty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "questions")
public class QuestionEntity {
    @Id
    private String id;
    private String framework;
    private String language;
    private Specialty specialty;
    private String statement;
    private QuestionLevel level;
}