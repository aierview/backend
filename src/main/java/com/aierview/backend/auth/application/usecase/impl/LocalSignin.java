package com.aierview.backend.auth.application.usecase.impl;

import com.aierview.backend.auth.application.usecase.contract.ILocalSignin;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;

import java.util.Optional;

public class LocalSignin implements ILocalSignin {
    private final IUserRepository userRepository;

    public LocalSignin(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String execute(LocalSigninRequest request) {
        Optional<UserRef> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new EmailAlreadyInUseException(request.getEmail());
        return "";
    }
}
