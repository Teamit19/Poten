package com.example.poten.domain;

import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.response.BoardResponse;
import com.example.poten.dto.response.CommentResponse;
import com.example.poten.dto.response.FileResponse;
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
        this.hearts = new ArrayList<HeartBoard>();
        this.comment = new ArrayList<Comment>();
        this.pics = new ArrayList<FileEntity>();
    }

    /* Entity -> DTO */
    public BoardResponse toResponse(){
        // 리스트 형식인 필드를  DTO로 변환
        List<HeartBoardResponse> heartsResponses = new ArrayList<>();
        if (! hearts.isEmpty()) hearts.forEach(h -> heartsResponses.add(h.toResponse()));


        List<CommentResponse> commentResponses = new ArrayList<>();
        if (! comment.isEmpty())  comment.forEach(h -> commentResponses.add(h.toResponse()));

        var FileResponse = new FileResponse();
        if (! pics.isEmpty()) FileResponse = pics.get(0).toResponse();


        return BoardResponse.builder()
            .boardId(id)
            .writer(user.toResponse())
            .club(club.toResponse())
            .content(content)
            .hearts(heartsResponses)
            .comment(commentResponses)
            .heartsNum(hearts.size())
            .commentsNum(comment.size())
            .pics(FileResponse)
            .createdTime(getCreatedTime().toString())
            .modifiedTime(getModifiedTime().toString())
            .build();
    }

    // 피드 글 수정하기
    public void update(BoardForm form, List<FileEntity> pics)  {
        this.content = form.getContent();
        this.pics = pics;
    }

    public void setPics(List<FileEntity> pics) {
        this.pics = pics;
    }
}
