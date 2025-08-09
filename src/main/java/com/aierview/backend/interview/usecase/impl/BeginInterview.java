package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGenerateInterview;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;

public class BeginInterview implements IBeginInterview {
    private final IGetLoggedUser getLoggedUser;
    private final IGenerateInterview generateInterview;

    public BeginInterview(IGetLoggedUser getLoggedUser, IGenerateInterview generateInterview) {
        this.getLoggedUser = getLoggedUser;
        this.generateInterview = generateInterview;
    }

    @Override
    public Interview execute(BeginInterviewRequest request) {
        UserRef user = this.getLoggedUser.execute();
        this.generateInterview.execute(request,user);
        return null;
    }
}
