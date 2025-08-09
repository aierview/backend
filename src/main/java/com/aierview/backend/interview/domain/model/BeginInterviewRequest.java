package com.aierview.backend.interview.domain.model;

import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
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
    private String stack;
}
