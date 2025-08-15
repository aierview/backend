package com.aierview.backend.interview.infra.cache;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.adapter.cache.InterviewCacheRepositoryAdapter;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.Mockito.*;

public class InterviewCacheRepositoryAdapterTests {
    private IInterviewCacheRepository interviewCacheRepository;
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        this.redisTemplate = mock(RedisTemplate.class);
        this.valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        this.interviewCacheRepository = new InterviewCacheRepositoryAdapter(redisTemplate);
    }

    @Test
    @DisplayName("Should add interview to cache")
    void shouldAddInterviewToCache() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);
        List<Question> questions =  InterviewTestFixture.anyQuestionList(savedInterview);
        savedInterview.setQuestions(questions);

        this.interviewCacheRepository.put(savedInterview);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(valueOperations).set(eq("interview:" + savedInterview.getId()), captor.capture());

        Object captured = captor.getValue();

        assertNotNull(captured);
        assertInstanceOf(InterviewState.class, captured);
        InterviewState state = (InterviewState) captured;
        assertEquals(savedInterview.getId(), state.getInterviewId());
        assertEquals(questions, state.getQuestions());
    }


    @Test
    @DisplayName("Should get cached interview state")
    void shouldGetCachedInterviewState() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);
        List<Question> questions =  InterviewTestFixture.anyQuestionList(savedInterview);
        savedInterview.setQuestions(questions);

        InterviewState savedInterviewState = new InterviewState(savedInterview.getId(),savedInterview.getQuestions());

        when(valueOperations.get("interview:" + savedInterview.getId())).thenReturn(savedInterviewState);

        InterviewState result = this.interviewCacheRepository.get(savedInterview.getId());

        assertNotNull(result);
        assertEquals(savedInterview.getId(), result.getInterviewId());
        assertEquals(savedInterview.getQuestions(), result.getQuestions());
    }

    @Test
    @DisplayName("Should remove interview on cache")
    void shouldRemoveInterviewOnCache() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);


        this.interviewCacheRepository.put(savedInterview);
        this.interviewCacheRepository.remove(savedInterview.getId());
        InterviewState result = this.interviewCacheRepository.get(savedInterview.getId());

        Assertions.assertNull(result);
    }
}
