package com.example.poten.dto.response;

import com.example.poten.domain.Comment;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.HeartBoard;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BoardResponse {

    private Long boardId;
    private UserResponse writer;
    private ClubResponse club;
    private String content;

    private List<HeartBoardResponse> hearts;
    private List<CommentResponse> comment;
//    private List<FileEntity> pics;  // <<TODO>>

    private String createdTime;
    private String modifiedTime;
}
