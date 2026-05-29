package com.KamyEsm.AAA.Service.Permission;

import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.Entity.Role;

import java.util.List;

public interface PermissionService {
    Permission create(Permission newPermission);
    Permission getById(Long id);
    void deleteById(Long id);
    Permission UpdateById(Long id , Permission permission);
    List<Permission> getAll(int page , int count);
    Permission getByName(String permissionName);
}
