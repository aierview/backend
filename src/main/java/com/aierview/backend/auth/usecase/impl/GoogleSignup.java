package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.google.IExtractUserDetails;
import com.aierview.backend.auth.usecase.contract.IGoogleSignup;

public class GoogleSignup implements IGoogleSignup {
    private final IExtractUserDetails extractUserDetails;

    public GoogleSignup(IExtractUserDetails extractUserDetails) {
        this.extractUserDetails = extractUserDetails;
    }

    @Override
    public void execute(String idToken) {
        this.extractUserDetails.extract(idToken).orElseThrow(InvalidGoogleIdTokenException::new);
    }
}
