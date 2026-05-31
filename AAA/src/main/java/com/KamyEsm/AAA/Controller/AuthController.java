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
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.ok(userMapper.toDto(registerService.register(userMapper.toEntity(request))));
    }


    @GetMapping("/login")
    public String loginView() {
        return "loginpage";
    }

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            Model model
    ) {
        try {
            var authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authResult = authenticationManager.authenticate(authRequest);

            // ست کردن کانتکست
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            // ذخیره در session تا برای authorize endpoint لاگین “بماند”
            request.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            // موفقیت: معمولاً redirect (برای جلوگیری از re-post)
            return "redirect:/";
        } catch (AuthenticationException ex) {
            // بدون redirect: همان صفحه رندر شود
            model.addAttribute("loginError", true);
            model.addAttribute("errorMessage", "نام کاربری یا رمز عبور اشتباه است");
            return "loginpage";
        }
    }

}
