package com.example.poten.dto.request;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.Poster;
import com.example.poten.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PosterForm {
    private Long clubId;

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    private LocalDateTime deadlineDate;

    private List<FileEntity> posterImg;

    /* DTO -> Entity */
    public Poster toPoster(User writer, Club club){
        return Poster.builder()
            .club(club)
            .user(writer)
            .title(this.title)
            .content(this.content)
            .deadlineDate(this.deadlineDate)
            .posterImg(this.posterImg)
            .build();
    }
}
