package com.example.poten.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HeartBoardResponse {
    private Long heartId;
    private UserResponse lover;
    private BoardResponse board;
}
