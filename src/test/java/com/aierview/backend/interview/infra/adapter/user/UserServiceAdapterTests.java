package com.aierview.backend.interview.infra.adapter.user;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.interview.domain.contract.user.IGetLoggedUser;
import com.aierview.backend.interview.domain.exceptions.UserNotAuthenticatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class UserServiceAdapterTests {
    private IGetLoggedUser getLoggedUser;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userMapper = Mockito.mock(UserMapper.class);
        this.getLoggedUser = new UserServiceAdapter(userMapper);
    }

    @Test
    @DisplayName("Should throw UserNotAuthenticatedException when user is not authenticated")
    void shouldThrowUserNotAuthenticatedExceptionWhenUserIsNotAuthenticated() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);


        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn("qualquer-coisa");
        SecurityContextHolder.setContext(securityContextMock);

        Throwable exception = catchThrowable(() -> this.getLoggedUser.execute());

        assertThat(exception).isInstanceOf(UserNotAuthenticatedException.class);
        assertThat(exception.getMessage()).isEqualTo("User is not authenticated!");
    }

    @Test
    @DisplayName("Should return user if is  authenticated")
    void shouldThrowReturnUserIfIsAuthenticated() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);


        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(new UserJpaEntity());
        SecurityContextHolder.setContext(securityContextMock);

        Mockito.when(this.userMapper.mapToEntity(Mockito.any(UserJpaEntity.class)))
                .thenReturn(Mockito.mock(UserRef.class));

        UserRef result = this.getLoggedUser.execute();
        assertThat(result).isNotNull();
    }
}
