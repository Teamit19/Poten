package com.example.poten.dto.response;

import com.example.poten.domain.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    private String email;

    private String name;

    private String nickname;

    private Integer sex;

    private String birth;

    private String phone;

    private String school;

//    @OneToMany
//    private List<FileEntity> profile;




}
