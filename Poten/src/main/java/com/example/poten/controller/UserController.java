package com.example.poten.controller;

import com.example.poten.dto.SignUpForm;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpForm signUpForm) {
        log.info("회원가입 버튼 눌림");

    }
}
