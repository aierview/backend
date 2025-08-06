package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;

public class LocalSignin implements ILocalSignin {
    private final IUserRepository userRepository;

    public LocalSignin(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String execute(LocalSigninRequest request) {
        this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialException::new);
        return "";
    }
}
