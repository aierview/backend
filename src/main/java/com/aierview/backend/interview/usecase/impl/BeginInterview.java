package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;

public class BeginInterview implements IBeginInterview {
    private final IGetLoggedUser getLoggedUser;

    public BeginInterview(IGetLoggedUser getLoggedUser) {
        this.getLoggedUser = getLoggedUser;
    }

    @Override
    public Interview execute(BeginInterviewRequest request) {
        this.getLoggedUser.execute();
        return null;
    }
}
