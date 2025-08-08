package com.aierview.backend.auth.usecase.impl.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.token.ITokenGenerator;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;
import com.aierview.backend.auth.usecase.contract.cookie.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignin;

public class GoogleSignin implements IGoogleSignin {
    private final IExtractUserDetails extractUserDetails;
    private final IUserRepository userRepository;
    private final ITokenGenerator tokenGenerator;
    private final IGenerateCookieResponse generateCookieResponse;

    public GoogleSignin(IExtractUserDetails extractUserDetails, IUserRepository userRepository,
                        ITokenGenerator tokenGenerator, IGenerateCookieResponse generateCookieResponse) {
        this.extractUserDetails = extractUserDetails;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.generateCookieResponse = generateCookieResponse;
    }

    @Override
    public CookieResponse execute(GoogleAuhRequest request) {
        GoogleAccountModel googleAccountModel = this.extractUserDetails.extractUserDetails(request)
                .orElseThrow(InvalidGoogleIdTokenException::new);

        UserRef user = this.userRepository.findByEmail(googleAccountModel.email())
                .orElseThrow(InvalidCredentialException::new);
        String token = this.tokenGenerator.generate(user);
        return this.generateCookieResponse.generate("token", token);
    }
}
