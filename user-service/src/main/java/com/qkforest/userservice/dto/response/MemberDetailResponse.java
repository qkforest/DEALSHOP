package com.qkforest.userservice.dto.response;

import com.qkforest.userservice.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailResponse {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;

    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
                .email(member.getName())
                .password(member.getPassword())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .build();
    }
}
