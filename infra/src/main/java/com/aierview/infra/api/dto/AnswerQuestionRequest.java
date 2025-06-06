package com.aierview.infra.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuestionRequest {

    @NotNull(message = "AnswerText is required!")
    @NotBlank(message = "AnswerText is required!")
    private String answerText;
}
