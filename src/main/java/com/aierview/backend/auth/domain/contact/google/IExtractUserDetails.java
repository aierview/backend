package com.aierview.backend.auth.domain.contact.google;

import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;

import java.util.Optional;

public interface IExtractUserDetails {
    Optional<GoogleAccountModel> extractUserDetails(GoogleAuhRequest request);
}
