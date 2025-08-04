package com.aierview.backend.auth.domain.model;

import com.aierview.backend.auth.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class LocalSignupRequest {

    @Schema(example = "example@example")
    @NotNull(message = "Email is required!")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format!"
    )
    private String email;

    @Schema(example = "John Snow")
    @NotNull(message = "Name is required!")
    @Size(min = 5, message = "Please provide your full name!")
    @Pattern(
            regexp = "^(?=\\p{L}+\\s+\\p{L}+)[\\p{L} ]+$",
            message = "Please provide your full name!"
    )
    private String name;

    @Schema(example = "MOBILE")
    @NotNull(message = "Role is required!")
    private UserRole role;

    @Schema(example = "Password123!")
    @NotNull(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters long and must contain at least one uppercase letter, one number, and one special character!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./]).{6,}$",
            message = "Password must be at least 6 characters long and must contain at least one uppercase letter, one number, and one special character!"
    )
    private String password;
}
