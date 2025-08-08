package com.aierview.backend.auth.usecase.contract.lcoal;

import com.aierview.backend.auth.domain.model.LocalSignupRequest;

public interface ILocalSignup {
    void execute(LocalSignupRequest request);
}
