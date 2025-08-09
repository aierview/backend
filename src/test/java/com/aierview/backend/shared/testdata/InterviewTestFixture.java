package com.aierview.backend.shared.testdata;

import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

public class InterviewTestFixture {
    public static BeginInterviewRequest anyBeginInterviewRequest() {
        return BeginInterviewRequest
                .builder()
                .interviewLevel(InterviewLevel.MIDLEVEL)
                .role(InterviewRole.FULLSTACK)
                .stack("any stack")
                .build();
    }
}
