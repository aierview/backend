package com.aierview.infra.api.dto;

import com.aierview.domain.enums.QuestionLevel;
import com.aierview.domain.enums.Specialty;
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
public class GenerateQuestionRequest {
    @NotNull(message = "Framework type is required!")
    @NotBlank(message = "Framework type is required!")
    private String framework;

    @NotNull(message = "Language type is required!")
    @NotBlank(message = "Language type is required!")
    private String language;

    @NotNull(message = "Specialty type is required!")
    private Specialty specialty;

    @NotNull(message = "Level type is required!")
    private QuestionLevel level;
}