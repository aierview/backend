package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.auth.domain.entity.UserRef;

public interface IGetLoggedUser {
    UserRef execute();
}
