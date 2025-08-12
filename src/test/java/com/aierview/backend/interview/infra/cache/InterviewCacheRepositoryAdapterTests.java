package com.aierview.backend.interview.infra.cache;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.infra.adapter.cache.InterviewCacheRepositoryAdapter;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InterviewCacheRepositoryAdapterTests {
    private IInterviewCacheRepository interviewCacheRepository;

    @BeforeEach
    void setUp() {
        this.interviewCacheRepository = new InterviewCacheRepositoryAdapter();
    }

    @Test
    @DisplayName("Should add interview to cache")
    void shouldAddInterviewToCache() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);


        this.interviewCacheRepository.put(savedInterview);
        InterviewState result = this.interviewCacheRepository.get(savedInterview.getId());

        Assertions.assertEquals(toSaveInterview.getId(), result.getInterviewId());
    }
}
