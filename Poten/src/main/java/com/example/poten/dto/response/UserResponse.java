package com.example.poten.dto.response;

import com.example.poten.domain.FileEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private Integer sex;
    private String birth;
    private String phone;
    private String school;
   // private List<FileEntity> profile; // <<TODO>>
}
