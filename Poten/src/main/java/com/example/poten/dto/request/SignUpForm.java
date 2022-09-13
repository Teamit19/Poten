package com.example.poten.dto.request;

import com.example.poten.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SignUpForm {

    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;





    @Builder
    public User toUser() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }


}
