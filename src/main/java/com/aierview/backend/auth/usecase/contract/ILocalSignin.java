package com.aierview.backend.auth.usecase.contract;

import com.aierview.backend.auth.domain.model.CookieResponse;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;

public interface ILocalSignin {
    CookieResponse execute(LocalSigninRequest request);
}
