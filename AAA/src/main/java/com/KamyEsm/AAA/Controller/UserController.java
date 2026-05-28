package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UpdateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest user){
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.toEntity(user))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<List<UserResponse>> getAllByPageAndCount(@RequestParam(defaultValue = "0" , required = false) int page ,
                                                                   @RequestParam(defaultValue = "15" , required = false) int count){
        return ResponseEntity.ok(mapper.toDTOList(service.getAll(page , count)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id , @RequestBody @Valid UpdateUserRequest user){
        return ResponseEntity.ok(mapper.toDto(service.UpdateById(id,mapper.toEntity(user))));
    }

}
