package com.qkforest.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$", message = "비밀번호는 최소 8자, 최대 20자, 영문, 숫자, 특수문자 조합으로 이루어져야 합니다.")
    private String password;
}
