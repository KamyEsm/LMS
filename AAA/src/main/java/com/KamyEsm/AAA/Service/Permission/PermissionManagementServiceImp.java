package com.KamyEsm.AAA.Service.Permission;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.Mapper.PermissionMapper;
import com.KamyEsm.AAA.Service.Role.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionManagementServiceImp implements PermissionManagementService{

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final PermissionMapper permissionMapper;

    @Override
    public Set<String> addPermissionToRole(String permissionName, String roleName) {
        Role role = roleService.getByName(roleName);
        Permission permission = permissionService.getByName(permissionName);

        Set<Permission> permissions = role.getPermissions();
        permissions.add(permission);

        return permissions.stream().map(Permission::getName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> disablePermissionFromRole(String permissionName, String roleName) {
        Role role = roleService.getByName(roleName);
        Permission permission = permissionService.getByName(permissionName);

        Set<Permission> permissionSet = role.getPermissions().stream()
                .filter(p -> !Objects.equals(p.getName(), permissionName)).
                collect(Collectors.toSet());

        return permissionSet.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
