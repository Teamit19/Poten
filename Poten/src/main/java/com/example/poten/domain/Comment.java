package com.example.poten.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
