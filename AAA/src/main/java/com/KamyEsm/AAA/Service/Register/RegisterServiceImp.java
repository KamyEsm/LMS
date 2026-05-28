package com.KamyEsm.AAA.Service.Register;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.Enum.InitialRole;
import com.KamyEsm.AAA.Service.Role.RoleService;
import com.KamyEsm.AAA.Service.User.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterServiceImp implements RegisterService{

    private final RoleService roleService;
    private final UserService userService;

    @Override
    public MyUser register(MyUser newUser) {
        Role role = roleService.getByName(InitialRole.ROLE_MEMBER.name());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        newUser.setRoles(roleSet);
        return userService.create(newUser);
    }

}
