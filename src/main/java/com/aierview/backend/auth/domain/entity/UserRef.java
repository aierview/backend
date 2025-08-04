package com.aierview.backend.auth.domain.entity;

import com.aierview.backend.auth.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRef {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
