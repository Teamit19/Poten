package com.example.poten.domain;

import com.example.poten.dto.request.SignUpForm;
import com.example.poten.dto.response.UserResponse;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@Getter
public class User{

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

    @OneToOne
    private FileEntity profile;

    public User socialUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public void update(SignUpForm signUpForm) {
        this.name = signUpForm.getName();
    }

    public UserResponse toResponse() {
        return UserResponse.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname)
                .sex(this.sex)
                .birth(this.birth)
                .phone(this.phone)
                .school(this.school)
                .build();
    }

    @Builder
    public User(Long id, String email, String password, String name, String nickname, Integer sex, String birth, String phone, String school, FileEntity profile) {
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



}
