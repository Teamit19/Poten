package com.example.poten.dto.response;

import com.example.poten.domain.FileEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PosterResponse {

    private Long posterId;
    private ClubResponse club;
    private UserResponse writer;
    private String title;
    private String content;
    private String deadlineDate;
   // private List<FileEntity> posterImg; // <<TODO>>

    private String createdTime;
    private String modifiedTime;
}
