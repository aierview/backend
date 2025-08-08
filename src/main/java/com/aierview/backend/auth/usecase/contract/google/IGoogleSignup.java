package com.aierview.backend.auth.usecase.contract.google;

public interface IGoogleSignup {
    void execute(String idToken);
}
