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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PosterForm {
    private Long clubId;

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deadlineDate;

    private List<MultipartFile> posterImg;

    /* DTO -> Entity */
    public Poster toPoster(User writer, Club club, List<FileEntity> pics){
        return Poster.builder()
            .club(club)
            .user(writer)
            .title(this.title)
            .content(this.content)
            .deadlineDate(this.deadlineDate)
            .posterImg(pics)
            .build();
    }
}
