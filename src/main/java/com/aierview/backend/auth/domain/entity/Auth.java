package com.aierview.backend.auth.domain.entity;

import com.aierview.backend.auth.domain.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    private Long id;
    private String password;
    private AuthProvider provider;
    private String picture;
    private UserRef user;
}
