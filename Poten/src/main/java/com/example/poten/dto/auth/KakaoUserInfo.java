package com.example.poten.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {

    private Long id;

    private String kakaoEmail;

    private String ageRange;

    private String gender;



}
