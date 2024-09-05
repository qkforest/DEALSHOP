package com.qkforest.userservice.dto.response;

import com.qkforest.userservice.domain.User;
import com.qkforest.userservice.util.AES256;
import lombok.Data;

@Data
public class UpdateUserInfoResponse {
    private String phoneNumber;
    private String address;

    public UpdateUserInfoResponse(User user, AES256 aes256) {
        this.phoneNumber = aes256.decrypt(user.getPhoneNumber());
        this.address = aes256.decrypt(user.getAddress());
    }
}
