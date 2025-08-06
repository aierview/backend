package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.domain.security.IPasswordComparer;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;

public class LocalSignin implements ILocalSignin {
    private final IUserRepository userRepository;
    private final IAuthRepository authRepository;
    private final IPasswordComparer passwordComparer;

    public LocalSignin(IUserRepository userRepository, IAuthRepository authRepository, IPasswordComparer passwordComparer) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordComparer = passwordComparer;
    }

    @Override
    public String execute(LocalSigninRequest request) {
        UserRef user =  this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialException::new);
        Auth auth =  this.authRepository.findByUserId(user.getId()).get();
        if(!passwordComparer.matches(request.getPassword(), auth.getPassword()))
            throw new InvalidCredentialException();

        return "";
    }
}
