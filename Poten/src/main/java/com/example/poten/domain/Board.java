package com.example.poten.domain;

import com.example.poten.dto.request.SaveBoardForm;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> hearts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> pics;

    @Builder
    public Board(User user, Club club, String content,
        List<User> hearts, List<Comment> comment,
        List<FileEntity> pics) {
        this.user = user;
        this.club = club;
        this.content = content;
        this.hearts = hearts;
        this.comment = comment;
        this.pics = pics;
    }

    // 피드 글 수정하기
    public void update(SaveBoardForm form) {
        this.content = form.getContent();
        this.pics = form.getPics();
    }

    // 피드 좋아요 누르기
    public boolean addLike(User user){
        hearts.add(user);
        return true;
    }

    // 피드 좋아요 취소하기
//    public boolean deleteLike(User user){
//        hearts.add(user);
//        return true;
//    }

    // 피드 댓글 달기

}
