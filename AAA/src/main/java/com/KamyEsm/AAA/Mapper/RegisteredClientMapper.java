package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Oauth2RegisteredClientEntity;
import com.KamyEsm.AAA.Entity.Permission;
import org.mapstruct.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@Mapper(componentModel = "spring")
public interface RegisteredClientMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientSecret", ignore = true)
    Oauth2RegisteredClientEntity toEntity(RegisteredClient dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RegisteredClient toDto(Oauth2RegisteredClientEntity entity);

    default String mapToScopeString(Permission permission){
        if(permission != null && !permission.getName().isBlank())
            return permission.getName();
        else return null;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void update(RegisteredClient registeredClient , @MappingTarget Oauth2RegisteredClientEntity entity);

}
