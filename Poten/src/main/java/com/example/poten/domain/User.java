package com.example.poten.domain;

import com.example.poten.dto.request.SignUpForm;
import com.example.poten.dto.response.UserResponse;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User implements UserDetails {

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

    private String role;


    @OneToMany
    private List<FileEntity> profile;

    public User socialUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public void update(SignUpForm signUpForm) {
        this.name = signUpForm.getName();
        this.nickname = signUpForm.getNickName();
        this.birth = signUpForm.getBirth();
        this.phone = signUpForm.getPhone();
        this.school = signUpForm.getSchool();
        this.sex = signUpForm.getSex();

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
        this.role = "ROLE_USER";
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
