package com.example.poten.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> like;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> pics;

}
