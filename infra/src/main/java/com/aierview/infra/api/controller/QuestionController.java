package com.aierview.infra.api.controller;

import com.aierview.application.usecase.contract.IGenerateQuestion;
import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.domain.exceptions.UnexpectedException;
import com.aierview.infra.api.dto.GenerateQuestionRequest;
import com.aierview.infra.api.dto.Response;
import com.aierview.infra.api.mapper.QuestionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/questions")
@Tag(name = "Questions", description = "Questions")
public class QuestionController {
    private final IGenerateQuestion generateQuestion;
    private final QuestionMapper mapper;

    @PostMapping
    @Operation(summary = "Generate Question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> create(@Valid @RequestBody GenerateQuestionRequest request) throws UnexpectedException {
        GenerateQuestionParams params = mapper.map(request, GenerateQuestionParams.class);
        var questions = generateQuestion.generate(params);
        Response response = Response.builder().statusCode(HttpStatus.CREATED.value()).data(questions).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
