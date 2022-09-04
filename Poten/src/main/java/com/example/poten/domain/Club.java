package com.example.poten.domain;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Table(name="clubs")
public class Club extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name="managerId")
    private User user;

    private String clubDesc;

    private String region;

    private Integer field;

    private Integer activityType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> follows;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> hearts;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Poster> posters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> members;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> waiting;

    @Builder
    public Club(Long id, String name, User user, String clubDesc, String region,
        Integer field, Integer activityType, List<User> follows,
        List<User> hearts, List<Board> boards, List<Poster> posters,
        List<User> members, List<User> waiting) {
     this.id = id;
     this.name = name;
     this.user = user;
     this.clubDesc = clubDesc;
     this.region = region;
     this.field = field;
     this.activityType = activityType;
     this.follows = new ArrayList<User>();
     this.hearts = new ArrayList<User>();
     this.boards = new ArrayList<Board>();
     this.posters = new ArrayList<Poster>();
     this.members = new ArrayList<User>();
     this.waiting = new ArrayList<User>();
    }
   /**
    * 동아리 멤버 로직
    *
    */
    // 동아리 회원 추가
    public long addMember(User user){
       members.add(user);
       return user.getId();
    }

    /**
     * 피드 (게시물) 로직
     * 
     */
    // 새 게시물 업로드
    public void addBoard(Board board) {
        boards.add(board);
    }
}
