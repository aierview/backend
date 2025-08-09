package com.aierview.backend.shared.testdata;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.enums.InterviewStatus;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewTestFixture {
    public static BeginInterviewRequest anyBeginInterviewRequest() {
        return BeginInterviewRequest
                .builder()
                .interviewLevel(InterviewLevel.MIDLEVEL)
                .role(InterviewRole.FULLSTACK)
                .stack("any stack")
                .build();
    }

    public static Interview anyInterviewWithNoQuestions(UserRef savedUser) {

        return Interview
                .builder()
                .stack("any stack")
                .user(savedUser)
                .role(InterviewRole.FULLSTACK)
                .level(InterviewLevel.MIDLEVEL)
                .status(InterviewStatus.CREATED)
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
    }

    public static Interview anySavedInterviewWithNoQuestions(Interview anyInterviewWithNoQuestions) {
        anyInterviewWithNoQuestions.setId(1L);
        return anyInterviewWithNoQuestions;
    }

    public static Question anyQuestion () {
        return Question.builder().question("any_question").build();
    }

    public  static List<Question> anyQuestionList () {
        return List.of(anyQuestion(), anyQuestion());
    }
}
