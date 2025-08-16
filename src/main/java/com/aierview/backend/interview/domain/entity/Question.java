package com.aierview.backend.interview.domain.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"interview"})
public class Question {
    private Long id;
    private String question;
    private String audioUrl;
    private String answer;
    private String feedback;
    private Interview interview;
}
