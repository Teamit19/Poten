package com.example.poten.dto;

import com.example.poten.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@AllArgsConstructor
@Builder
@Getter
public class SignUpForm {

    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickName;

    @NotNull(message = "성별은 필수 입력값입니다.")
    private Integer sex;

    @NotBlank(message = "생년월일은 필수 입력값입니다.")
    private String birth;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "소속은 필수 입력값입니다.")
    private String school;

    @Builder
    public User toUser(String encryptedPassword) {
        return User.builder()
                .email(this.email)
                .password(encryptedPassword)
                .name(this.name)
                .nickname(this.nickName)
                .sex(this.sex)
                .birth(this.birth)
                .phone(this.phone)
                .school(this.school)
                .build();
    }


}
