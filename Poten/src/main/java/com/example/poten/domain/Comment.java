package com.example.poten.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    private Long id;

    @JoinColumn(name="userId")
    private User user;

    @JoinColumn(name="boardId")
    private Board board;

    private String content;

    public Comment() {
    }
}
