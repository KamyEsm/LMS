package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.DTO.login.LoginRequest;
import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Service.JWT.JWTGenerateService;
import com.KamyEsm.AAA.Service.Register.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerateService jwtService;
    private final RegisterService registerService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    @PreAuthorize("hasAuthority('CREAT_USER')")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.ok(userMapper.toDto(registerService.register(userMapper.toEntity(request))));
    }


    @GetMapping("/login")
    public String loginView() {
        return "loginpage";
    }

}
