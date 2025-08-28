package com.aierview.backend.auth.usecase.contract.google;

import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;

public interface IGoogleSignup {
    void execute(GoogleAuhRequest request);
}
