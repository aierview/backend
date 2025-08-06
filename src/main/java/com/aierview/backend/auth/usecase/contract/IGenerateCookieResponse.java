package com.aierview.backend.auth.usecase.contract;

import com.aierview.backend.auth.domain.model.CookieResponse;

public interface IGenerateCookieResponse {
    CookieResponse generate(String name, String value);
}
