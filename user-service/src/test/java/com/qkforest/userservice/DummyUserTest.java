package com.qkforest.userservice;

import com.qkforest.userservice.domain.User;
import com.qkforest.userservice.domain.RoleEnum;
import com.qkforest.userservice.repositroy.UserRepository;
import com.qkforest.userservice.util.AES256;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class DummyUserTest {

    @Autowired
    private AES256 aes256;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("1000명 유저 생성")
    public void saveUsers()  {
        for (int i = 1; i <= 1000; i++) {
            String name = "test" + String.valueOf(i);
            String email = name + "@naver.com";
            userRepository.save(User.builder()
                    .email(aes256.encrypt(email))
                    .name(aes256.encrypt(name))
                    .password(passwordEncoder.encode("123abc!!"))
                    .phoneNumber(aes256.encrypt("010-1234-5678"))
                    .address(aes256.encrypt("서울시서대문구"))
                    .role(RoleEnum.USER)
                    .build());
        }
    }
}
