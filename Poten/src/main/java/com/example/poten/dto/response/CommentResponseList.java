package com.example.poten.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResponseList {
    public List<CommentResponse> CommentResponseList;
}
