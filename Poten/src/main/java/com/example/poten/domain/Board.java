package com.example.poten.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Board extends BaseTimeEntity {

    @Id
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

    @OneToMany(mappedBy = "boards", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> like;

    @OneToMany(mappedBy = "boards", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> pics;

}
