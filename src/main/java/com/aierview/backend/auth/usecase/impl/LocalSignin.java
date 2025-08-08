package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.security.IPasswordComparer;
import com.aierview.backend.auth.domain.contact.token.ITokenGenerator;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.CookieResponse;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.usecase.contract.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;

public class LocalSignin implements ILocalSignin {
    private final IUserRepository userRepository;
    private final IAuthRepository authRepository;
    private final IPasswordComparer passwordComparer;
    private final ITokenGenerator tokenGenerator;
    private final IGenerateCookieResponse generateCookieResponse;

    public LocalSignin(IUserRepository userRepository, IAuthRepository authRepository, IPasswordComparer passwordComparer, ITokenGenerator tokenGenerator, IGenerateCookieResponse generateCookieResponse) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordComparer = passwordComparer;
        this.tokenGenerator = tokenGenerator;
        this.generateCookieResponse = generateCookieResponse;
    }

    @Override
    public CookieResponse execute(LocalSigninRequest request) {
        UserRef user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialException::new);
        Auth auth = this.authRepository.findByUserId(user.getId()).get();
        if (!passwordComparer.matches(request.getPassword(), auth.getPassword()))
            throw new InvalidCredentialException();
        String token = this.tokenGenerator.generate(user);
        return this.generateCookieResponse.generate("token", token);
    }
}
