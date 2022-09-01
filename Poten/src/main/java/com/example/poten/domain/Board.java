package com.example.poten.domain;

import javax.persistence.*;
import java.util.List;

public class Board extends BaseTimeEntity {

    @Id
    private Long id;

    @JoinColumn(name="userId")
    private User user;

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
