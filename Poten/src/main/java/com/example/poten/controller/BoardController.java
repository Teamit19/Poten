package com.example.poten.controller;

import com.example.poten.dto.request.SignUpForm;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

    @ApiOperation(value = "게시물 업로드")
    @PostMapping("/upload")
    public void uploadBoard(@RequestBody SignUpForm signUpForm) {
//        log.info("회원가입 버튼 눌림");

    }
}
