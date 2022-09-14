package com.example.poten.controller;

import com.example.poten.config.SessionManager;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.SignUpForm;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    @PostMapping("")
    public ResponseEntity<?> signUp(HttpServletRequest request, @RequestBody SignUpForm signUpForm) throws LoginException {
        log.info("회원가입 버튼 눌림");
        User loginUser = userService.getLoginUser(request);
        Boolean saveResult = userService.signUp(signUpForm, loginUser.getId());

        return ResponseEntity.ok(new BoolResponse(saveResult));

    }

    @ApiOperation(value = "관심키워드 입력")
    @PostMapping("/keyword")
    public ResponseEntity<?> interestKeyword(HttpServletRequest request, @RequestBody Map<String, List<String>> interestMap) throws LoginException {
        log.info("회원가입 버튼 눌림");
        User loginUser = userService.getLoginUser(request);
        List<String> interestList = interestMap.get("interestList");

        Boolean saveResult = userService.saveInterest(loginUser, interestList);

        return ResponseEntity.ok(new BoolResponse(saveResult));

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

    @ApiOperation(value = "현재 로그인 회원 정보 조회")
    @GetMapping("")
    public ResponseEntity<?> getMe(HttpServletRequest request) throws LoginException {
        log.info("회원정보 조회");

        User loginUser = userService.getLoginUser(request);
        log.info("현재 로그인한 사용자"+ loginUser.getEmail());

        return ResponseEntity.ok(loginUser.toResponse());

    }


}

