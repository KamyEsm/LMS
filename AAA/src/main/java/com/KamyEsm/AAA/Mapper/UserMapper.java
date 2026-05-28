package com.KamyEsm.AAA.Mapper;

import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UpdateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateUserFromDto(MyUser dto, @MappingTarget MyUser entity);


    UserResponse toDto(MyUser user);

    MyUser toEntity(CreateUserRequest userDto);

    MyUser toEntity(UpdateUserRequest userDto);

    default String mapRoletoString(Role role){
        if(role == null) return null;
        else return role.getName();
    }

    List<UserResponse> toDTOList(List<MyUser> myUserList);

}

