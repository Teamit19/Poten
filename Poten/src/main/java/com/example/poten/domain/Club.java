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
public class Club extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name="managerId")
    private User user;

    private String desc;

    private String region;

    private Integer field;

    private Integer activityType;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Follow> follows;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> hearts;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Poster> posters;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> members;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<User> waiting;

}
