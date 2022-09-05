package com.example.poten.controller;

import com.example.poten.dto.request.SignUpForm;
import com.example.poten.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @ApiOperation(value = "게시물 업로드")
    @PostMapping("/upload")
    public void uploadBoard(@RequestBody SignUpForm signUpForm) {
//        log.info("회원가입 버튼 눌림");
    }



}
