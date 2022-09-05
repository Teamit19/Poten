package com.example.poten.domain;

import com.example.poten.dto.response.UserResponse;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@Getter
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
    private FileEntity profile;

    @Builder
    public User(Long id, String email, String password, String name, String nickname, Integer sex, String birth, String phone, String school, List<FileEntity> profile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.sex = sex;
        this.birth = birth;
        this.phone = phone;
        this.school = school;
        this.profile = profile;
    }

    /* Entity -> DTO */
    public UserResponse toResponse(){
        return UserResponse.builder()
            .memberId(id)
            .email(email)
            .name(name)
            .nickname(nickname)
            .sex(sex)
            .birth(birth)
            .phone(phone)
            .school(school)
            .build();
    }
}
