package com.KamyEsm.AAA.Service.Role;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.ExceptionHandling.NotFoundException;
import com.KamyEsm.AAA.Mapper.RoleMapper;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Repository.RoleRepository;
import com.KamyEsm.AAA.Repository.UserRepository;
import com.KamyEsm.AAA.Service.User.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleManagementServiceImp implements RoleManagementService {

    private final RoleService roleService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Set<String> addRoleToUser(String roleName , String username) {
        MyUser user = userService.getByUserName(username);
        Role role = roleService.getByName(roleName);


        Set<Role> roleSet = user.getRoles();
        roleSet.add(role);

        Set<String> rStrings = new HashSet<>();

        for (Role r : roleSet){
            rStrings.add(userMapper.mapRoletoString(r));
        }

        return rStrings;
    }

    @Override
    public Set<String> disableRoleFromUser(String roleName , String username) {
        MyUser user = userService.getByUserName(username);
        Set<Role> roleSet = user.getRoles().stream().filter(c -> !Objects.equals(c.getName(), roleName)).collect(Collectors.toSet());

        Set<String> rStrings = new HashSet<>();
        for (Role r : roleSet){
            rStrings.add(userMapper.mapRoletoString(r));
        }

        return rStrings;
    }
}
