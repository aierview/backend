package com.aierview.backend.auth.domain.contact.google;

import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;

import java.util.Optional;

public interface IExtractUserDetails {
    Optional<GoogleAccountModel> extract(String token);
}
