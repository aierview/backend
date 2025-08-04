package com.aierview.backend.auth.application.usecase.contract;

import com.aierview.backend.auth.domain.model.LocalSigninRequest;

public interface ILocalSignup {
    void execute(LocalSigninRequest request);
}
