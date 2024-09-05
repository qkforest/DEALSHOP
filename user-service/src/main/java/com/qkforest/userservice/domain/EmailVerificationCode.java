package com.qkforest.userservice.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "emailVerificationCode", timeToLive = 1800)
public class EmailVerificationCode {

    @Id
    @Indexed
    private String emailVerificationCode;
    private String email;
}
