package com.example.poten.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HeartClubResponse {
    private Long heartId;
    private UserResponse lover;
    private ClubResponse club;
}
