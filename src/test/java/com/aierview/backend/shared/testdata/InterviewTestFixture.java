package com.aierview.backend.shared.testdata;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persisntence.entity.UserJpaEntity;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.enums.InterviewStatus;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import org.mockito.Mockito;

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

    public static InterviewJpaEntity anyInterviewJpaEntityWithNoQuestions(Interview interview) {
        UserJpaEntity userJpaEntity = AuthTestFixture.anyUserJpaEntity(interview.getUser());
        return InterviewJpaEntity
                .builder()
                .stack("any stack")
                .user(userJpaEntity)
                .role(InterviewRole.FULLSTACK)
                .level(InterviewLevel.MIDLEVEL)
                .status(InterviewStatus.CREATED)
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
    }

    public static InterviewJpaEntity anySavedInterviewJpaEntityWithNoQuestions(InterviewJpaEntity toSaveEntity) {
        toSaveEntity.setId(1L);
        return toSaveEntity;
    }

    public static Interview anySavedInterviewWithNoQuestions(InterviewJpaEntity savedInterviewJpaEntity) {
        UserRef userRef = AuthTestFixture.anySavedUser(savedInterviewJpaEntity.getUser());
        return Interview
                .builder()
                .id(savedInterviewJpaEntity.getId())
                .stack(savedInterviewJpaEntity.getStack())
                .user(userRef)
                .role(savedInterviewJpaEntity.getRole())
                .level(savedInterviewJpaEntity.getLevel())
                .status(savedInterviewJpaEntity.getStatus())
                .createdAt(savedInterviewJpaEntity.getCreatedAt())
                .build();
    }

    public static Interview anySavedInterviewWithNoQuestions(Interview anyInterviewWithNoQuestions) {
        anyInterviewWithNoQuestions.setId(1L);
        return anyInterviewWithNoQuestions;
    }

    public static Question anyQuestion(Interview anySavedInterview) {
        return Question.builder().interview(anySavedInterview).question("any_question").build();
    }

    public static Question anySavedQuestion(Interview anySavedInterview) {
        return Question.builder().id(1L).interview(anySavedInterview).question("any_question").build();
    }

    public static List<Question> anyQuestionList(Interview anySavedInterview) {
        return List.of(anyQuestion(anySavedInterview), anyQuestion(anySavedInterview));
    }

    public static List<Question> anySavedQuestionList(Interview anySavedInterview) {
        return List.of(anySavedQuestion(anySavedInterview), anySavedQuestion(anySavedInterview));
    }

    public static Interview anySavedStartedInterviewWithQuestions(Interview anyInterviewWithNoQuestions, List<Question> questions) {
        anyInterviewWithNoQuestions.setStatus(InterviewStatus.STARTED);
        anyInterviewWithNoQuestions.setQuestions(questions);
        return anyInterviewWithNoQuestions;
    }
}
