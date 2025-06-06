package com.aierview.infra.api.controller;

import com.aierview.application.usecase.contract.question.IAnswerQuestion;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.exceptions.NotFoundException;
import com.aierview.infra.api.dto.AnswerQuestionRequest;
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
@RequestMapping("/api/v1/questions")
@Tag(name = "Questions", description = "Questions")
public class QuestionController {
    private final IAnswerQuestion answerQuestion;
    private final QuestionMapper mapper;

    @PostMapping("/{questionId}")
    @Operation(summary = "Answer Question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> answer(@PathVariable("questionId") String questionId,
                                           @Valid @RequestBody AnswerQuestionRequest request) throws NotFoundException {
        Answer answer = mapper.map(request, Answer.class);
        answer.setQuestionId(questionId);
        answerQuestion.answer(answer);
        Response response = Response.builder().statusCode(HttpStatus.OK.value()).data("OK").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
