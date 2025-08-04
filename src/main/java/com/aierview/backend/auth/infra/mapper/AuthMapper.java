package com.aierview.backend.auth.infra.mapper;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.AuthJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    AuthJpaEntity authToAuthJpaEntity(Auth auth);

    Auth authJpaEntityToAuth(AuthJpaEntity auth);
}
