package com.aierview.backend.auth.usecase.impl.local;

import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.security.IPasswordEncoder;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSignupRequest;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignup;
import com.aierview.backend.shared.utils.FuncUtils;

import java.util.Optional;

public class LocalSignup implements ILocalSignup {
    private final IUserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;
    private final IAuthRepository authRepository;

    public LocalSignup(IUserRepository userRepository, IPasswordEncoder passwordEncoder, IAuthRepository authRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
    }

    @Override
    public void execute(LocalSignupRequest request) {
        this.formatRequest(request);
        Optional<UserRef> existingUser = this.userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new EmailAlreadyInUseException(request.getEmail());
        UserRef user = UserRef
                .builder().email(request.getEmail()).name(request.getName()).role(request.getRole()).build();
        user = this.userRepository.save(user);
        String encodedPassword = this.passwordEncoder.encode(request.getPassword());
        Auth auth = Auth.builder().provider(AuthProvider.LOCAL).user(user).password(encodedPassword).build();
        this.authRepository.save(auth);
    }

    private void formatRequest(LocalSignupRequest request) {
        String email = FuncUtils.formatLowercase(request.getEmail());
        request.setEmail(email);
        String name = FuncUtils.formatToCapitalize(request.getName());
        request.setName(name);
    }
}
