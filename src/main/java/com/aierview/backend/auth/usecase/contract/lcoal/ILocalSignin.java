package com.aierview.backend.auth.usecase.contract.lcoal;

import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;

public interface ILocalSignin {
    CookieResponse execute(LocalSigninRequest request);
}
