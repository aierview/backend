package com.aierview.infra.api.controller;

import com.aierview.application.usecase.contract.questionnaire.IGenerateQuestionare;
import com.aierview.application.usecase.contract.questionnaire.IGetFeedback;
import com.aierview.domain.entity.GenerateQuestionareParams;
import com.aierview.domain.exceptions.BusinessException;
import com.aierview.domain.exceptions.UnexpectedException;
import com.aierview.infra.api.dto.GenerateQuestionareRequest;
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
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/questionaries")
@Tag(name = "Questionare", description = "Questionare")
public class QuestionareController {
    private final IGenerateQuestionare generateQuestionare;
    private final IGetFeedback getFeedback;
    private final QuestionMapper mapper;

    @PostMapping
    @Operation(summary = "Generate Questionare")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> generate(@Valid @RequestBody GenerateQuestionareRequest request) throws UnexpectedException {
        GenerateQuestionareParams params = mapper.map(request, GenerateQuestionareParams.class);
        var questions = generateQuestionare.generate(params);
        Response response = Response.builder().statusCode(HttpStatus.CREATED.value()).data(questions).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{questionareId}")
    @Operation(summary = "Get Feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> get(@PathVariable("questionareId") String questionareId) throws BusinessException {
        var feedback = getFeedback.get(questionareId);
        Response response = Response.builder().statusCode(HttpStatus.OK.value()).data(feedback).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
