package com.qkforest.userservice.repositroy;

import com.qkforest.userservice.domain.EmailVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
