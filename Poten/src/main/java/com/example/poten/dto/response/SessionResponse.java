package com.example.poten.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SessionResponse {
    private UserResponse userResponse;
    private String sessionId;

}
