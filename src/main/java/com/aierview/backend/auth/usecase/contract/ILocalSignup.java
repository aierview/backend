package com.aierview.backend.auth.usecase.contract;

import com.aierview.backend.auth.domain.model.LocalSignupRequest;

public interface ILocalSignup {
    void execute(LocalSignupRequest request);
}
