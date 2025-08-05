package com.aierview.backend.auth.infra.mapper;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserJpaEntity userRefToUserJpaEntity(UserRef user);

    UserRef userJpaEntityToUserRef(UserJpaEntity user);
}
