package com.example.poten.dto.request;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.User;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class BoardForm {

    private Long clubId;

    @NotEmpty
    private String content;

    private List<MultipartFile> pics;

    @Builder
    public BoardForm(Long clubId, String content,
        List<MultipartFile> pics) {
        this.clubId = clubId;
        this.content = content;
        this.pics = pics;
    }

    /* DTO -> Entity */
    public Board toBoard(User writer, Club club, List<FileEntity> pics){
        return Board.builder()
                    .user(writer)
                    .content(this.content)
                    .club(club)
                    .pics(pics)
                    .build();
    }
}
