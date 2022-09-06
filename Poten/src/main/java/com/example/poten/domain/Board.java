package com.example.poten.domain;

import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.response.BoardResponse;
import com.example.poten.dto.response.CommentResponse;
import com.example.poten.dto.response.HeartBoardResponse;
import com.example.poten.dto.response.HeartClubResponse;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name="board")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="clubId")
    private Club club;

    private String content;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeartBoard> hearts;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> pics;

    @Builder
    public Board(Long id, User user, Club club, String content,
        List<HeartBoard> hearts, List<Comment> comment,
        List<FileEntity> pics) {
        this.id = id;
        this.user = user;
        this.club = club;
        this.content = content;
        this.hearts = hearts;
        this.comment = comment;
        this.pics = pics;
    }

    /* Entity -> DTO */
    public BoardResponse toResponse(){
        // 리스트 형식인 필드를  DTO로 변환
        List<HeartBoardResponse> heartsResponses = new ArrayList<>();
        hearts.forEach(h -> heartsResponses.add(h.toResponse()));

        List<CommentResponse> commentResponses = new ArrayList<>();
        comment.forEach(h -> commentResponses.add(h.toResponse()));

        return BoardResponse.builder()
            .boardId(id)
            .writer(user.toResponse())
            .club(club.toResponse())
            .content(content)
            .hearts(heartsResponses)
            .comment(commentResponses)
            .createdTime(getCreatedTime().toString())
            .modifiedTime(getModifiedTime().toString())
            .build();
    }

    // 피드 글 수정하기
    public void update(BoardForm form) {
        this.content = form.getContent();
        this.pics = form.getPics();
    }

}
