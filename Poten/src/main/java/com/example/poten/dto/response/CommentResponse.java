package com.example.poten.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long commentId;
    private UserResponse writer;
    private BoardResponse board;
    private String content;

    private String createdTime;
    private String modifiedTime;

}
