package com.example.poten.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoolResponse { // 결과를 bool값을 전달할 때 사용하는 responseDto
    boolean result;
}
