package com.example.poten.domain;

import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.ClubResponse;
import com.example.poten.dto.response.FileResponse;
import com.example.poten.dto.response.HeartClubResponse;
import com.example.poten.dto.response.PosterResponse;
import com.example.poten.dto.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.ArrayList;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
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
    private User manager;

    private String clubDesc;

    private String region;

    private String field;

    private String activityType;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> profile;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> background;


    @JsonIgnore
    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Follow> following;

    @JsonIgnore
    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeartClub> hearts;

    @JsonIgnore
    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @JsonIgnore
    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Poster> posters;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> members;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> waitings;

    @Builder
    public Club(Long id, String name, User manager, String clubDesc, String region,
                String field, String activityType, List<FileEntity> profile, List<FileEntity> background, List<Follow> following,
                List<HeartClub> hearts, List<Board> boards, List<Poster> posters,
                List<User> members, List<User> waitings) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.clubDesc = clubDesc;
        this.region = region;
        this.field = field;
        this.activityType = activityType;
        this.profile = profile;
        this.background = background;
        this.following = new ArrayList<Follow>();
        this.hearts = new ArrayList<HeartClub>();
        this.boards = new ArrayList<Board>();
        this.posters = new ArrayList<Poster>();
        this.members = new ArrayList<User>();
        this.waitings = new ArrayList<User>();
    }

    public void setProfile(List<FileEntity> profile) {
        this.profile = profile;
    }

    public void setBackground(List<FileEntity> background) {
        this.background = background;
    }

    /* Entity -> DTO */
    public ClubResponse toResponse() {
        // 리스트 형식인 필드를  DTO로 변환
//        List<UserResponse> followsResponses = new ArrayList<>();
//        following.forEach(f -> followsResponses.add(f.getFollower().toResponse()));
//
//        List<HeartClubResponse> heartsResponses = new ArrayList<>();
//        hearts.forEach(h -> heartsResponses.add(h.toResponse()));
//
//        List<PosterResponse> postersResponses = new ArrayList<>();
//        posters.forEach(p -> postersResponses.add(p.toResponse()));
//
        List<UserResponse> membersResponses = new ArrayList<>();
        members.forEach(m -> membersResponses.add(m.toResponse()));
//
//        List<UserResponse> waitingsResponses = new ArrayList<>();
//        waitings.forEach(m -> waitingsResponses.add(m.toResponse()));

        var fileResponse = new FileResponse();
        if (! profile.isEmpty()) fileResponse = profile.get(0).toResponse();

        var fileResponse2 = new FileResponse();
        if (! background.isEmpty()) fileResponse2 = background.get(0).toResponse();

        return ClubResponse.builder()
                .clubId(id)
                .clubName(name)
                .manager(manager.toResponse())
                .clubDesc(clubDesc)
                .region(region)
                .field(field)
                .activityType(activityType)
                .followersNum(following.size())
                .heartsNum(hearts.size())
                .membersNum(members.size())
//                .follows(followsResponses)
//                .hearts(heartsResponses)
//                .posters(postersResponses)
                .members(membersResponses)
//                .waitings(waitingsResponses)
                .profile(fileResponse)
                .background(fileResponse2)
                .createdTime(getCreatedTime().toString())
                .build();
    }

    /**
     * 동아리 멤버 로직
     */
    // 동아리 회원 추가
    public long addMember(User user) {
        members.add(user);
        waitings.remove(user);
        return user.getId();
    }

    public void deleteMember(User user) {
        members.remove(user);

    }

    public long addWaiting(User user) {
        waitings.add(user);
        return user.getId();
    }

//    public long addFollower(User user) {
//        follows.add(user);
//        return user.getId();
//    }
//
//    public void removeFollower(User user) {
//        follows.remove(user);
//
//    }

    public void updateProfile(List<FileEntity> profile) {
        this.profile = profile;
    }

    public void updateBackground(List<FileEntity> background) {
        this.background = background;
    }



    public void update(ClubForm form) {
        this.clubDesc = form.getClubDesc();
        this.name = form.getName();
        this.region = form.getRegion();
        this.activityType = form.getActivityType();
        this.field = form.getField();
    }

    public void changeManager(User user) {
        this.manager = user;
    }

    /**
     * 피드 (게시물) 로직
     */
    // 새 게시물 업로드
    public void addBoard(Board board) {
        boards.add(board);
    }
}
