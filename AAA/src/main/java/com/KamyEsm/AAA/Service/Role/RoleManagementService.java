package com.KamyEsm.AAA.Service.Role;

import java.util.Set;

public interface RoleManagementService {
    Set<String> addRoleToUser(String roleName , String username);
    Set<String> disableRoleFromUser(String roleName , String username);
}
