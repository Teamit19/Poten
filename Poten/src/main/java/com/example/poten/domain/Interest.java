package com.example.poten.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interest;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Builder
    public Interest(String interest, User user) {
        this.interest = interest;
        this.user = user;
    }




}
