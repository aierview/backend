package com.aierview.backend.interview.domain.contract.user;

import com.aierview.backend.auth.domain.entity.UserRef;

public interface IGetLoggedUser {
    UserRef execute();
}
