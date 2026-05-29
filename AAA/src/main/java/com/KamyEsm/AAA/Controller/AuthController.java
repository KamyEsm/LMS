package com.KamyEsm.AAA.Controller;

import com.KamyEsm.AAA.DTO.login.LoginRequest;
import com.KamyEsm.AAA.DTO.user.CreateUserRequest;
import com.KamyEsm.AAA.DTO.user.UserResponse;
import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Service.JWT.JWTGenerateService;
import com.KamyEsm.AAA.Service.Register.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerateService jwtService;
    private final RegisterService registerService;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateJWTToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.ok(userMapper.toDto(registerService.register(userMapper.toEntity(request))));
    }




}
