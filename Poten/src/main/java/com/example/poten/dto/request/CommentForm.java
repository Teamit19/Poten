package com.example.poten.dto.request;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentForm {
    @NotEmpty
    private String content;

}
