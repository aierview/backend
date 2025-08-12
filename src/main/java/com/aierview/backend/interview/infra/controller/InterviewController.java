package com.aierview.backend.interview.infra.controller;

import com.aierview.backend.auth.domain.model.http.Response;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
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
@RequestMapping("/api/v1/interview")
@Tag(name = "Interview", description = "Interview Features")
public class InterviewController {
    private final IBeginInterview beginInterviewUseCase;

    @PostMapping("/begin")
    @Operation(summary = "Begin Interview")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> beginInterviewUse(@Valid @RequestBody BeginInterviewRequest request) {
        this.beginInterviewUseCase.execute(request);
        Response response = Response.builder().data("202").statusCode(HttpStatus.ACCEPTED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
