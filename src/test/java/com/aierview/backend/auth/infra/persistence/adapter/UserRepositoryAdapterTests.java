package com.aierview.backend.auth.infra.persistence.adapter;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persisntence.adapter.UserRepositoryAdapter;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.UserJpaRepository;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryAdapterTests {
    private UserRepositoryAdapter userRepositoryAdapter;
    private UserJpaRepository userJpaRepository;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userJpaRepository = mock(UserJpaRepository.class);
        this.userMapper = mock(UserMapper.class);
        this.userRepositoryAdapter = new UserRepositoryAdapter(userJpaRepository, userMapper);
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

    @Test
    @DisplayName("Should return Optional UserRef when user exists on find by email")
    void shouldReturnUserRefWhenUserExistsOnFindByEmail() {
        String email = "any_email";
        UserJpaEntity savedUserJpaEntity = AuthTestFixture.anySavedUserJpaEntity(email);
        UserRef savedUserRef = AuthTestFixture.anyUserRef(savedUserJpaEntity);

        when(this.userJpaRepository.findByEmail(email)).thenReturn(Optional.of(savedUserJpaEntity));
        when(this.userMapper.userJpaEntityToUserRef(savedUserJpaEntity)).thenReturn(savedUserRef);

        Optional<UserRef> result = this.userRepositoryAdapter.findByEmail(email);

        Assertions.assertEquals(savedUserRef.getId(), result.get().getId());
        Assertions.assertEquals(savedUserRef.getName(), result.get().getName());
        Assertions.assertEquals(savedUserRef.getEmail(), result.get().getEmail());
        Assertions.assertEquals(savedUserRef.getRole(), result.get().getRole());
        verify(this.userJpaRepository, times(1)).findByEmail(email);
    }
}
