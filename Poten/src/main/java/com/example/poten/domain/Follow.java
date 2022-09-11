package com.example.poten.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clubId")
    private Club following;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User follower;

    @Builder
    public Follow(Long id, Club following, User follower) {
        this.id = id;
        this.following = following;
        this.follower = follower;
    }

    public void addFollow(Club following, User follower) {
        this.following = following;
        this.follower = follower;
    }




}
