package com.aierview.backend.auth.domain.model;

import com.aierview.backend.auth.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalSigninRequest {
    private String email;
    private String name;
    private UserRole role;
    private String password;
}
