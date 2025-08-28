package com.aierview.backend.auth.infra.adapter.repository;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.auth.infra.persistence.repository.UserJpaRepository;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

        assertTrue(result.isEmpty());
        verify(this.userJpaRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should return Optional UserRef when user exists on find by email")
    void shouldReturnUserRefWhenUserExistsOnFindByEmail() {
        String email = "any_email";
        UserJpaEntity savedUserJpaEntity = AuthTestFixture.anySavedUserJpaEntity(email);
        UserRef savedUserRef = AuthTestFixture.anyUserRef(savedUserJpaEntity);

        when(this.userJpaRepository.findByEmail(email)).thenReturn(Optional.of(savedUserJpaEntity));
        when(this.userMapper.mapToEntity(savedUserJpaEntity)).thenReturn(savedUserRef);

        Optional<UserRef> result = this.userRepositoryAdapter.findByEmail(email);

        assertEquals(savedUserRef.getId(), result.get().getId());
        assertEquals(savedUserRef.getName(), result.get().getName());
        assertEquals(savedUserRef.getEmail(), result.get().getEmail());
        assertEquals(savedUserRef.getRole(), result.get().getRole());
        verify(this.userJpaRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should save user entity and return user ref")
    void shouldSaveUserEntityAndReturnUserRef() {
        UserRef userRef = AuthTestFixture.anyUserRef();
        UserJpaEntity entity = AuthTestFixture.anyUserJpaEntity(userRef);
        UserJpaEntity savedEntity = AuthTestFixture.anyUserJpaEntity(entity);
        UserRef savedUserRef = AuthTestFixture.anyUserRef(savedEntity);

        when(this.userMapper.mapToJpa(userRef)).thenReturn(entity);
        when(this.userJpaRepository.save(entity)).thenReturn(savedEntity);
        when(this.userMapper.mapToEntity(savedEntity)).thenReturn(savedUserRef);

        UserRef result = this.userRepositoryAdapter.save(userRef);

        assertEquals(savedUserRef.getId(), result.getId());
        assertEquals(savedUserRef.getName(), result.getName());
        assertEquals(savedUserRef.getEmail(), result.getEmail());
        assertEquals(savedUserRef.getRole(), result.getRole());
        verify(this.userJpaRepository, times(1)).save(entity);
    }
}
