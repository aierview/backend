package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.model.CookieResponse;
import com.aierview.backend.auth.usecase.contract.IGenerateCookieResponse;
import com.aierview.backend.shared.enums.Environment;

public class GenerateCookieResponse implements IGenerateCookieResponse {
    private final Environment environment;

    public GenerateCookieResponse(Environment environment) {
        this.environment = environment;
    }

    @Override
    public CookieResponse generate(String name, String value) {
        String sameSite = this.environment.equals(Environment.PROD) || this.environment.equals(Environment.HOMOLOG)? "NONE" : "LAX";
        boolean secure = this.environment.equals(Environment.PROD) || this.environment.equals(Environment.HOMOLOG);
        return new CookieResponse(name, value, true, secure, sameSite, "/");
    }
}
