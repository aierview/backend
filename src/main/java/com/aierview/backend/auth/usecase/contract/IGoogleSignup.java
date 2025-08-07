package com.aierview.backend.auth.usecase.contract;

public interface IGoogleSignup {
    void execute(String idToken);
}
