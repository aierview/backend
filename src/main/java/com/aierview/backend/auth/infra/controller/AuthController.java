package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.http.Response;
import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;
import com.aierview.backend.auth.domain.model.local.LocalSignupRequest;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignup;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignin;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignup;
import com.aierview.backend.shared.utils.FuncUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final ILocalSignin localSigninUseCase;
    private final IGoogleSignup googleSignupUseCase;

    @PostMapping("/local/signup")
    @Operation(summary = "Local signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> localSignup(@Valid @RequestBody LocalSignupRequest request) {
        this.localSignupUseCase.execute(request);
        Response response = Response.builder().data("Created").statusCode(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/local/signin")
    @Operation(summary = "Local signin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> localSignin(@Valid @RequestBody LocalSigninRequest request, HttpServletResponse httPResponse) {
        CookieResponse cookieResponse = this.localSigninUseCase.execute(request);
        String cookie = FuncUtils.cookieFromCookieResponse(cookieResponse);
        Response response = Response.builder().data("OK").statusCode(HttpStatus.OK.value()).build();
        httPResponse.addHeader(HttpHeaders.SET_COOKIE, cookie);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/google/signup")
    @Operation(summary = "Google signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> googleSignup(
            @RequestBody @NotBlank( message = "Id token is required!") String idToken) {
        this.googleSignupUseCase.execute(idToken);
        Response response = Response.builder().data("Created").statusCode(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
