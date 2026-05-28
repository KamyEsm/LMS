package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.DTO.Role.CreateRoleRequest;
import com.KamyEsm.AAA.DTO.Role.RoleResponse;
import com.KamyEsm.AAA.DTO.Role.UpdateRoleRequest;
import com.KamyEsm.AAA.Mapper.RoleMapper;
import com.KamyEsm.AAA.Service.Role.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;
    private final RoleMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<RoleResponse> create(@RequestBody @Valid CreateRoleRequest role){
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(role))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public ResponseEntity<List<RoleResponse>> getAllByPageAndCount(@RequestParam(defaultValue = "0" , required = false) int page ,
                                                                   @RequestParam(defaultValue = "15" , required = false) int count){
        return ResponseEntity.ok(mapper.toDTOList(service.getAll(page , count)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id , @RequestBody @Valid UpdateRoleRequest role){
        return ResponseEntity.ok(mapper.toDto(service.UpdateById(id,mapper.toEntity(role))));
    }

}
