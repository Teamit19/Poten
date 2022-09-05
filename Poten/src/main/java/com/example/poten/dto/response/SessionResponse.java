package com.example.poten.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionResponse {
    private UserResponse userResponse;
    private String sessionId;

}
