package com.aierview.backend.interview.infra.adapter.user;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persisntence.entity.UserJpaEntity;
import com.aierview.backend.interview.domain.contract.user.IGetLoggedUser;
import com.aierview.backend.interview.domain.exceptions.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceAdapter implements IGetLoggedUser {
    private final UserMapper userMapper;

    @Override
    public UserRef execute() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) throw new UserNotAuthenticatedException();
        return this.userMapper.userJpaEntityToUserRef((UserJpaEntity) principal);
    }
}
