package com.example.poten.domain;

import com.example.poten.dto.request.CommentForm;
import com.example.poten.dto.request.SaveBoardForm;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="boardId")
    private Board board;

    private String content;


    @Builder
    public Comment(Long id, User user, Board board, String content) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.content = content;
    }

    // 피드 글 수정하기
    public void update(CommentForm form) {
        this.content = form.getContent();
    }
}
