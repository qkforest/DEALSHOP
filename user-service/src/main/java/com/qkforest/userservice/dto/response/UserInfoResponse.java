package com.qkforest.userservice.dto.response;

import com.qkforest.userservice.domain.User;
import com.qkforest.userservice.util.AES256;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoResponse {
    private String email;
    private String name;
    private String phoneNumber;
    private String address;

    public static UserInfoResponse from(User user, AES256 aes256) {

        return UserInfoResponse.builder()
                .email(aes256.decrypt(user.getEmail()))
                .name(aes256.decrypt(user.getName()))
                .phoneNumber(aes256.decrypt(user.getPhoneNumber()))
                .address(aes256.decrypt(user.getAddress()))
                .build();
    }
}
