package com.example.poten.controller;

import com.example.poten.config.SessionManager;
import com.example.poten.domain.User;
import com.example.poten.dto.request.SignUpForm;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SessionManager sessionManager;

    public UserController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @ApiOperation(value = "회원정보 입력")
    @PostMapping("/{id}")
    public ResponseEntity<?> signUp(@PathVariable Long id, @RequestBody SignUpForm signUpForm) {
        log.info("회원가입 버튼 눌림");
        userService.signUp(signUpForm, id);


        return ResponseEntity.ok(signUpForm);

    }

    @ApiOperation(value = "회원정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, HttpServletRequest request) throws LoginException {
        log.info("회원정보 조회");
        UserResponse userResponse = userService.getInfo(id);

        User loginUser = userService.getLoginUser(request);
        log.info("현재 로그인한 사용자"+ loginUser.getEmail());

        return ResponseEntity.ok(userResponse);

    }


}

