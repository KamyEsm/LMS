package com.KamyEsm.AAA.Service.Permission;


import java.util.Set;

public interface PermissionManagementService {
    Set<String> addPermissionToRole(String permissionName , String roleName);
    Set<String> disablePermissionFromRole(String permissionName , String roleName);
}
