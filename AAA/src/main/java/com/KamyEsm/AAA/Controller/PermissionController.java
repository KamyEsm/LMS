package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.DTO.Permission.CreatePermissionRequest;
import com.KamyEsm.AAA.DTO.Permission.PermissionResponse;
import com.KamyEsm.AAA.DTO.Permission.UpdatePermissionRequest;
import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UpdateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Mapper.PermissionMapper;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Service.Permission.PermissionService;
import com.KamyEsm.AAA.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService service;
    private final PermissionMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    public ResponseEntity<PermissionResponse> create(@RequestBody @Valid CreatePermissionRequest permission){
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(permission))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public ResponseEntity<List<PermissionResponse>> getAllByPageAndCount(@RequestParam(defaultValue = "0" , required = false) int page ,
                                                                   @RequestParam(defaultValue = "15" , required = false) int count){
        return ResponseEntity.ok(mapper.toDTOList(service.getAll(page , count)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
    public ResponseEntity<PermissionResponse> update(@PathVariable Long id , @RequestBody @Valid UpdatePermissionRequest user){
        return ResponseEntity.ok(mapper.toDto(service.UpdateById(id,mapper.toEntity(user))));
    }

}
