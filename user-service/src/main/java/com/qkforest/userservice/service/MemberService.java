package com.qkforest.userservice.service;

import com.qkforest.userservice.domain.EmailVerificationCode;
import com.qkforest.userservice.domain.Member;
import com.qkforest.userservice.dto.request.SignUpRequest;
import com.qkforest.userservice.exception.BusinessLogicException;
import com.qkforest.userservice.exception.ExceptionCode;
import com.qkforest.userservice.repositroy.EmailVerificationCodeRepository;
import com.qkforest.userservice.util.AES256;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final EmailService mailService;
    private final com.qkforest.userservice.repositroy.MemberRepository memberRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final AES256 aes256;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void createMember(SignUpRequest signUpRequest) {
        verifiedCode(signUpRequest.getEmailVerificationCode());
        createUserInRepository(signUpRequest);
    }

    private void verifiedCode(String code){
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findById(code)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMAIL_CODE_IS_NOT_SAME));
        emailVerificationCodeRepository.delete(emailVerificationCode);
    }

    private void createUserInRepository(SignUpRequest signUpRequest) {
        memberRepository.save(Member.builder()
                .email(aes256.encrypt(signUpRequest.getEmail()))
                .name(aes256.encrypt(signUpRequest.getName()))
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .phoneNumber(aes256.encrypt(signUpRequest.getPhoneNumber()))
                .address(aes256.encrypt(signUpRequest.getAddress()))
                .build());
    }


    public void sendVerificationCodeEmail(String to) {
        validateDuplicateEmail(to);
        String title = "DEALSHOP 이메일 인증 번호";
        String authCode = this.createEmailVerificationCode();
        emailVerificationCodeRepository.save(EmailVerificationCode.builder()
                .emailVerificationCode(authCode)
                .email(to)
                .build());
        mailService.sendEmail(to, title, authCode);

    }

    public void validateDuplicateEmail(String email) {
        Optional<Member> user = memberRepository.findByEmail(aes256.encrypt(email));
        if (user.isPresent()) {
            log.debug("MemberService.checkDuplicatedEmail exception occur email: {}", email);
            throw new BusinessLogicException(ExceptionCode.MEMBER_EMAIL_EXISTS);
        }
    }

    private String createEmailVerificationCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createEmailVerificationCode() exception occur");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }
}
