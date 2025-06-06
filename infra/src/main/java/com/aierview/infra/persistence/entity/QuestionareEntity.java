package com.aierview.infra.persistence.entity;

import com.aierview.domain.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "questionaries")
public class QuestionareEntity {
    private String id;
    private String title;
    private List<Question> questions;
}
