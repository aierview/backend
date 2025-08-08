package com.aierview.backend.auth.usecase.contract.google;

import com.aierview.backend.auth.domain.model.google.GoogleSignupRequest;

public interface IGoogleSignup {
    void execute(GoogleSignupRequest request);
}
