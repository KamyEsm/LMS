package com.KamyEsm.AAA.Service.Role;

import com.KamyEsm.AAA.Entity.Role;

import java.util.List;

public interface RoleService {
    Role create(Role newRole);
    Role getById(Long id);
    void deleteById(Long id);
    Role UpdateById(Long id , Role role);
    List<Role> getAll(int page , int count);
    Role getByName(String name);
}
