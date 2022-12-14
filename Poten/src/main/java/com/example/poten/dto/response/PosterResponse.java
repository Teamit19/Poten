package com.example.poten.dto.response;

import com.example.poten.domain.FileEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
public class PosterResponse {

    private Long posterId;
    private ClubResponse club;
    private UserResponse writer;
    private String title;
    private String content;
    private String deadlineDate;
    private FileResponse posterImg;

    private String createdTime;
    private String modifiedTime;

    private Integer dday;
}