package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.Service.Role.RoleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role_management")
public class RoleManagementController {

    private final RoleManagementService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_ROLE_MANAGEMENT')")
    public ResponseEntity<Set<String>> addRoleToUser(@RequestParam String username , @RequestParam String roleName){
        return ResponseEntity.ok(service.addRoleToUser(roleName , username));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_ROLE_MANAGEMENT')")
    public ResponseEntity<Set<String>> deleteRoleFromUser(@RequestParam String username , @RequestParam String roleName){
        return ResponseEntity.ok(service.disableRoleFromUser(roleName , username));
    }
}
