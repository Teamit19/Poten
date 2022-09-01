package com.example.poten.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private Integer sex;

    private String birth;

    private String phone;

    private String school;

    @OneToMany
    private List<FileEntity> profile;

//    @OneToMany(mappedBy = "clubs", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    private List<Club> clubList;



}
