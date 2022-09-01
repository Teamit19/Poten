package com.example.poten.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
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

    @OneToMany(mappedBy = "clubs", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Club> clubList;


}
