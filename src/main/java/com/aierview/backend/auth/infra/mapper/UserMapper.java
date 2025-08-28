package com.aierview.backend.auth.infra.mapper;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserJpaEntity mapToJpa(UserRef user);

    UserRef mapToEntity(UserJpaEntity user);
}
