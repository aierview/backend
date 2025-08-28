package com.aierview.backend.interview.domain.model;

import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeginInterviewRequest {
    private InterviewRole role;

    private InterviewLevel interviewLevel;

    @NotBlank(message = "Stack is required!")
    private String stack;
}
