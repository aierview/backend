package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.usecase.contract.IGoogleSignup;

import java.util.Optional;

public class GoogleSignup implements IGoogleSignup {
    private final IExtractUserDetails extractUserDetails;
    private final IUserRepository userRepository;

    public GoogleSignup(IExtractUserDetails extractUserDetails, IUserRepository userRepository) {
        this.extractUserDetails = extractUserDetails;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(String idToken) {
        var googleAccountModel = this.extractUserDetails.extract(idToken).orElseThrow(InvalidGoogleIdTokenException::new);
        Optional<UserRef>  userRef =  this.userRepository.findByEmail(googleAccountModel.email());
        if(userRef.isPresent()) throw new EmailAlreadyInUseException(googleAccountModel.email());
    }
}
