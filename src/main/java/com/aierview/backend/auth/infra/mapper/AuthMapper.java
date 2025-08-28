package com.aierview.backend.auth.infra.mapper;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthJpaEntity mapToJpa(Auth auth);

    Auth mapToEntity(AuthJpaEntity auth);
}
