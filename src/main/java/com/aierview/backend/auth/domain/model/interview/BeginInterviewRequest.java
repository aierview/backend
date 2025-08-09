package com.aierview.backend.auth.domain.model.interview;

import com.aierview.backend.auth.domain.enums.UserLevel;
import com.aierview.backend.auth.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeginInterviewRequest {
    private UserRole role;
    private UserLevel userLevel;
    private String stack;
}
