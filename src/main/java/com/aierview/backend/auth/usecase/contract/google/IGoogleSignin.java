package com.aierview.backend.auth.usecase.contract.google;

import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;

public interface IGoogleSignin {
    CookieResponse execute(GoogleAuhRequest request);
}
