package com.example.poten.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Club {

    @Id
    private Long id;

    private String name;

    private String password;

    private String desc;

    private String region;

    private Integer field;

    private String member;

    private String waiting;

    private String board;

    private String like;
}
