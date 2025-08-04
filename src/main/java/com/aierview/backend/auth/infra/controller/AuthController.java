package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.domain.model.LocalSignupRequest;
import com.aierview.backend.auth.domain.model.Response;
import com.aierview.backend.auth.usecase.contract.ILocalSignup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication Features")
public class AuthController {
    private final ILocalSignup localSignupUseCase;

    @PostMapping("/local/signup")
    @Operation(summary = "Local signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> localSignup(@Valid @RequestBody LocalSignupRequest request) {
        this.localSignupUseCase.execute(request);
        Response response = Response.builder().data("Created").statusCode(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
