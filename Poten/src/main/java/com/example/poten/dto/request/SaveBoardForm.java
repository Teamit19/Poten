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

@AllArgsConstructor
@Builder
@Getter
public class SaveBoardForm {

    @NotEmpty
    private String content;

    private List<FileEntity> pics;

    public Board toBoard(User writer, Club club){
        return Board.builder()
                    .user(writer)
                    .content(this.content)
                    .club(club)
                    .pics(this.pics)
                    .build();
    }
}
