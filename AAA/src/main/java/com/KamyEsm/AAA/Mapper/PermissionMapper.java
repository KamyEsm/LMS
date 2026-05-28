package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.DTO.Permission.CreatePermissionRequest;
import com.KamyEsm.AAA.DTO.Permission.PermissionResponse;
import com.KamyEsm.AAA.DTO.Permission.UpdatePermissionRequest;
import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermissionFromDto(Permission dto, @MappingTarget Permission entity);

    PermissionResponse toDto(Permission permission);

    Permission toEntity(CreatePermissionRequest userDto);

    Permission toEntity(UpdatePermissionRequest userDto);

    default String mapRoletoString(Role role){
        if(role == null) return null;
        else return role.getName();
    }

    List<PermissionResponse> toDTOList(List<Permission> permissionList);

}
