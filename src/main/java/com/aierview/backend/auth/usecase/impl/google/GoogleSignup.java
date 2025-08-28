package com.aierview.backend.auth.usecase.impl.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignup;

import java.util.Optional;

public class GoogleSignup implements IGoogleSignup {
    private final IExtractUserDetails extractUserDetails;
    private final IUserRepository userRepository;
    private final IAuthRepository authRepository;

    public GoogleSignup(IExtractUserDetails extractUserDetails,
                        IUserRepository userRepository, IAuthRepository authRepository) {
        this.extractUserDetails = extractUserDetails;
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    public void execute(GoogleAuhRequest request) {
        GoogleAccountModel googleAccountModel = this.extractUserDetails.extractUserDetails(request)
                .orElseThrow(InvalidGoogleIdTokenException::new);

        Optional<UserRef> existingUser = this.userRepository.findByEmail(googleAccountModel.email());
        if (existingUser.isPresent()) throw new EmailAlreadyInUseException(googleAccountModel.email());
        UserRef user = UserRef.builder().name(googleAccountModel.name()).email(googleAccountModel.email()).build();
        user = this.userRepository.save(user);
        Auth auth = Auth.builder().user(user).provider(AuthProvider.GOOGLE).picture(googleAccountModel.picture()).build();
        this.authRepository.save(auth);
    }
}
