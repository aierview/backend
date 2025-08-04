package com.aierview.backend.auth.infra.persistence.adapter;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persisntence.adapter.UserRepositoryAdapter;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryAdapterTests {
    private UserRepositoryAdapter userRepositoryAdapter;
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        this.userJpaRepository = mock(UserJpaRepository.class);
        this.userRepositoryAdapter =  new UserRepositoryAdapter(userJpaRepository);
    }

    @Test
    @DisplayName("Should return optional empty when user does not exists on find by email")
    void shouldReturnEmptyOptionalWhenUserDoesNotExistsOnFindByEmail() {
        String email = "any_email";

        when(this.userJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserRef> result = this.userRepositoryAdapter.findByEmail(email);

        Assertions.assertTrue(result.isEmpty());
        verify(this.userJpaRepository, times(1)).findByEmail(email);
    }
}
