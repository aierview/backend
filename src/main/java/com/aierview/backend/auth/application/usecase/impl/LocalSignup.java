package com.aierview.backend.auth.application.usecase.impl;

import com.aierview.backend.auth.application.usecase.contract.ILocalSignup;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;

import java.util.Optional;

public class LocalSignup implements ILocalSignup {
    private final IUserRepository userRepository;

    public LocalSignup(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(LocalSigninRequest request) {
        Optional<UserRef> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new EmailAlreadyInUseException(request.getEmail());
    }
}
