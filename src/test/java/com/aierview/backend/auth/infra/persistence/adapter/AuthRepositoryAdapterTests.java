package com.aierview.backend.auth.infra.persistence.adapter;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.AuthMapper;
import com.aierview.backend.auth.infra.persisntence.adapter.AuthRepositoryAdapter;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.AuthJpaRepository;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthRepositoryAdapterTests {
    private AuthRepositoryAdapter authRepositoryAdapter;
    private AuthMapper authMapper;
    private AuthJpaRepository authJpaRepository;

    @BeforeEach
    void setUp() {
        this.authMapper = mock(AuthMapper.class);
        this.authJpaRepository = mock(AuthJpaRepository.class);
        this.authRepositoryAdapter = new AuthRepositoryAdapter(authMapper, authJpaRepository);
    }

    @Test
    @DisplayName("Should map to AuthJpaEntity and save Auth details")
    void shouldMapToAuthJpaEntityAndSaveAuthDetails() {
        Auth auth = AuthTestFixture.anyAuth();
        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(auth);
        AuthJpaEntity savedAuthJpaEntity = AuthTestFixture.anySavedAuthJpaEntity(auth);
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedAuthJpaEntity);

        when(this.authMapper.authToAuthJpaEntity(auth)).thenReturn(authJpaEntity);
        when(this.authJpaRepository.save(authJpaEntity)).thenReturn(savedAuthJpaEntity);
        when(this.authMapper.authJpaEntityToAuth(savedAuthJpaEntity)).thenReturn(savedAuth);

        Auth result = this.authRepositoryAdapter.save(auth);

        assertEquals(savedAuthJpaEntity.getId(), result.getId());
        assertEquals(savedAuthJpaEntity.getPassword(), result.getPassword());
        assertEquals(savedAuthJpaEntity.getProvider(), result.getProvider());
        assertEquals(savedAuthJpaEntity.getUser().getId(), result.getUser().getId());
        verify(this.authJpaRepository, times(1)).save(authJpaEntity);
    }

    @Test
    @DisplayName("Should return optional empty when auth does not exist on find by user id")
    void shouldReturnOptionalEmptyWhenAuthDoesNotExistOnFindByUserId() {
        Long userId = 1L;
        when(this.authJpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<Auth> result = this.authRepositoryAdapter.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(this.authJpaRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should return optional of auth when auth exists on find by user id")
    void shouldReturnOptionalAuthWhenAuthExistsOnFindByUserId() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);
        AuthJpaEntity savedAuthJpaEntity = AuthTestFixture.anySavedAuthJpaEntity(savedAuth);

        when(this.authJpaRepository.findByUserId(savedUser.getId())).thenReturn(Optional.of(savedAuthJpaEntity));
        when(this.authMapper.authJpaEntityToAuth(savedAuthJpaEntity)).thenReturn(savedAuth);

        Optional<Auth> result = this.authRepositoryAdapter.findByUserId(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals(savedAuthJpaEntity.getId(), result.get().getId());
        assertEquals(savedAuthJpaEntity.getUser().getId(), result.get().getUser().getId());
        verify(this.authJpaRepository, times(1)).findByUserId(savedUser.getId());
        verify(this.authMapper, times(1)).authJpaEntityToAuth(savedAuthJpaEntity);
    }
}
