package com.aierview.backend.auth.infra.persistence.adapter;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.infra.mapper.AuthMapper;
import com.aierview.backend.auth.infra.persisntence.adapter.AuthRepositoryAdapter;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.AuthJpaRepository;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
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

        Assertions.assertEquals(savedAuthJpaEntity.getId(), result.getId());
        Assertions.assertEquals(savedAuthJpaEntity.getPassword(), result.getPassword());
        Assertions.assertEquals(savedAuthJpaEntity.getProvider(), result.getProvider());
        Assertions.assertEquals(savedAuthJpaEntity.getUser().getId(), result.getUser().getId());
        verify(this.authJpaRepository, times(1)).save(authJpaEntity);
    }
}
