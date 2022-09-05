package com.example.poten.domain;

import com.example.poten.dto.response.HeartBoardResponse;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HeartBoard {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="boardId")
    private Board board;

    @Builder
    public HeartBoard(Long id, User user, Board board) {
        this.id = id;
        this.user = user;
        this.board = board;
    }

    public HeartBoardResponse toResponse() {
        return HeartBoardResponse.builder()
            .heartId(id)
            .lover(user.toResponse())
            .board(board.toResponse())
            .build();
    }
}
