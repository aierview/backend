package com.aierview.backend.auth.domain.model;

import com.aierview.backend.auth.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalSigninRequest {

    @Schema(example = "example@example")
    @NotNull(message = "Email is required!")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format!"
    )
    private String email;

    @Schema(example = "Password123!")
    @NotNull(message = "Password is required!")
    private String password;
}