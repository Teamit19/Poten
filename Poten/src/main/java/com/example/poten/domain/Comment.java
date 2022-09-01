package com.example.poten.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
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

    public Comment() {
    }
}
