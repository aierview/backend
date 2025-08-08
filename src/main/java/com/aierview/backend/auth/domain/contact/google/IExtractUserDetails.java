package com.aierview.backend.auth.domain.contact.google;

import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleSignupRequest;

import java.util.Optional;

public interface IExtractUserDetails {
    Optional<GoogleAccountModel> extractUserDetails(GoogleSignupRequest request);
}
