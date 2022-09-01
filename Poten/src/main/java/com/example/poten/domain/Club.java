package com.example.poten.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Club extends BaseTimeEntity{

    @Id
    private Long id;

    private String name;

    @JoinColumn(name="managerId")
    private User user;

    private String desc;

    private String region;

    private Integer field;

    private String member;

    private String waiting;

    private String board;

    private String like;
}
