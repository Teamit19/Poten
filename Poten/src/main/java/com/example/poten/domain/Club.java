package com.example.poten.domain;

import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.ClubResponse;
import com.example.poten.dto.response.HeartClubResponse;
import com.example.poten.dto.response.PosterResponse;
import com.example.poten.dto.response.UserResponse;
import com.sun.istack.NotNull;

import java.util.ArrayList;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Table(name = "clubs")
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "managerId")
    private User user;

    private String clubDesc;

    private String region;

    private Integer field;

    private Integer activityType;

    @OneToOne
    private FileEntity profile;

    @OneToOne
    private FileEntity background;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> follows;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeartClub> hearts;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Poster> posters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> members;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> waitings;

    @Builder
    public Club(Long id, String name, User user, String clubDesc, String region,
                Integer field, Integer activityType, FileEntity profile, FileEntity background, List<User> follows,
                List<HeartClub> hearts, List<Board> boards, List<Poster> posters,
                List<User> members, List<User> waitings) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.clubDesc = clubDesc;
        this.region = region;
        this.field = field;
        this.activityType = activityType;
        this.profile = profile;
        this.background = background;
        this.follows = new ArrayList<User>();
        this.hearts = new ArrayList<HeartClub>();
        this.boards = new ArrayList<Board>();
        this.posters = new ArrayList<Poster>();
        this.members = new ArrayList<User>();
        this.waitings = new ArrayList<User>();
    }

    /* Entity -> DTO */
    public ClubResponse toResponse() {
        // 리스트 형식인 필드를  DTO로 변환
        List<UserResponse> followsResponses = new ArrayList<>();
        follows.forEach(f -> followsResponses.add(f.toResponse()));

        List<HeartClubResponse> heartsResponses = new ArrayList<>();
        hearts.forEach(h -> heartsResponses.add(h.toResponse()));

        List<PosterResponse> postersResponses = new ArrayList<>();
        posters.forEach(p -> postersResponses.add(p.toResponse()));

        List<UserResponse> membersResponses = new ArrayList<>();
        members.forEach(m -> membersResponses.add(m.toResponse()));

        List<UserResponse> waitingsResponses = new ArrayList<>();
        waitings.forEach(m -> waitingsResponses.add(m.toResponse()));

        return ClubResponse.builder()
                .clubId(id)
                .clubName(name)
                .manager(user.toResponse())
                .clubDesc(clubDesc)
                .region(region)
                .field(field)
                .activityType(activityType)
                .follows(followsResponses)
                .hearts(heartsResponses)
                .posters(postersResponses)
                .members(membersResponses)
                .waitings(waitingsResponses)
                .createdTime(getCreatedTime().toString())
                .build();
    }

    /**
     * 동아리 멤버 로직
     */
    // 동아리 회원 추가
    public long addMember(User user) {
        members.add(user);
        return user.getId();
    }

    public void deleteMember(User user) {
        members.remove(user);

    }

    public void update(ClubForm form) {
        this.clubDesc = form.getClubDesc();
        this.name = form.getName();
        this.region = form.getRegion();
        this.activityType = form.getActivityType();
        this.field = form.getField();
    }

    /**
     * 피드 (게시물) 로직
     */
    // 새 게시물 업로드
    public void addBoard(Board board) {
        boards.add(board);
    }
}
