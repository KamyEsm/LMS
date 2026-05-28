package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.DTO.Role.CreateRoleRequest;
import com.KamyEsm.AAA.DTO.Role.RoleResponse;
import com.KamyEsm.AAA.DTO.Role.UpdateRoleRequest;
import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromDto(Role dto, @MappingTarget Role entity);

    RoleResponse toDto(Role role);

    Role toEntity(CreateRoleRequest user);

    Role toEntity(UpdateRoleRequest user);

    List<RoleResponse> toDTOList(List<Role> all);
    default String mapUsertoString(MyUser user){
        if(user == null) return null;
        else return user.getUsername();
    }
    default String mapPermissiontoString(Permission permission){
        if(permission == null) return null;
        else return permission.getName();
    }
}
