package com.aierview.backend.interview.infra.adapter.cache;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InterviewCacheRepositoryAdapter implements IInterviewCacheRepository {
    private final ConcurrentHashMap<Long, InterviewState> cache = new ConcurrentHashMap<>();

    @Override
    public void put(Interview interview) {
        InterviewState state = new InterviewState(interview.getId(), interview.getQuestions());
        this.cache.put(interview.getId(), state);
    }

    @Override
    public InterviewState get(Long interviewId) {
        return this.cache.get(interviewId);
    }

    @Override
    public void remove(Long interviewId) {
        this.cache.remove(interviewId);
    }
}
