package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.Service.Permission.PermissionManagementService;
import com.KamyEsm.AAA.Service.Role.RoleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permission-management")
public class PermissionManagementController {
     private final PermissionManagementService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_PERMISSION_MANAGEMENT')")
    public ResponseEntity<Set<String>> addPermissionToROLE(@RequestParam String roleName , @RequestParam String permission){
        return ResponseEntity.ok(service.addPermissionToRole(permission,roleName));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_PERMISSION_MANAGEMENT')")
    public ResponseEntity<Set<String>> disablePermissionFromRole(@RequestParam String roleName , @RequestParam String permission){
        return ResponseEntity.ok(service.disablePermissionFromRole(permission,roleName));
    }
}
