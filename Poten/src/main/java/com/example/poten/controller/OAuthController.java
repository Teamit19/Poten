package com.example.poten.controller;

import com.example.poten.dto.response.SessionResponse;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final UserService userService;

    public OAuthController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "카카오 로그인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "카카오 로그인 성공")
            , @ApiResponse(code = 400, message = "카카오 로그인 실패")
    })
    @ResponseBody
    @GetMapping("/kakao/{token}")
    public ResponseEntity<?> kakaoCallback(@PathVariable String token, HttpServletRequest request) {

        SessionResponse sessionResponse = userService.kakaoLogin(token, request);
        return ResponseEntity.ok(sessionResponse);
    }

    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback2(@RequestParam String code, HttpServletRequest request) {
        SessionResponse sessionResponse = userService.kakaoLogin(code, request);

        return ResponseEntity.ok(sessionResponse);


    }


}
