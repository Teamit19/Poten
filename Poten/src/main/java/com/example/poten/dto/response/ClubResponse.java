package com.example.poten.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ClubResponse {
    private Long clubId;
    private String clubName;
    private UserResponse manager;
    private String clubDesc;
    private String region;
    private Integer field; // <<TODO>> ?
    private Integer activityType; // <<TODO>> ?

    private List<UserResponse> follows;
    private List<HeartClubResponse> hearts;
    private List<BoardResponse> boards;
    private List<PosterResponse> posters;
    private List<UserResponse> members;
    private List<UserResponse> waitings;

    private String createdTime;
}
