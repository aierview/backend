package com.aierview.backend.auth.infra.mapper;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.infra.persisntence.entity.AuthJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthJpaEntity authToAuthJpaEntity(Auth auth);

    Auth authJpaEntityToAuth(AuthJpaEntity auth);
}
