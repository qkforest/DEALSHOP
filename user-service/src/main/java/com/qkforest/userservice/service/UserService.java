package com.qkforest.userservice.service;

import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.userservice.domain.EmailVerificationCode;
import com.qkforest.userservice.domain.User;
import com.qkforest.userservice.domain.RoleEnum;
import com.qkforest.userservice.dto.request.SignUpRequest;
import com.qkforest.userservice.dto.request.UpdateUserInfoRequest;
import com.qkforest.userservice.dto.request.UpdatePasswordRequest;
import com.qkforest.userservice.dto.response.UpdateUserInfoResponse;
import com.qkforest.userservice.dto.response.UserInfoResponse;
import com.qkforest.userservice.repositroy.EmailVerificationCodeRepository;
import com.qkforest.userservice.repositroy.UserRepository;
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
public class UserService {

    private final EmailService mailService;
    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final AES256 aes256;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void createUser(SignUpRequest signUpRequest) {
        verifiedCode(signUpRequest.getEmailVerificationCode());
        createUserInRepository(signUpRequest);
    }

    private void verifiedCode(String code){
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findById(code)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMAIL_CODE_IS_NOT_SAME));
        emailVerificationCodeRepository.delete(emailVerificationCode);
    }

    private void createUserInRepository(SignUpRequest signUpRequest) {
        userRepository.save(User.builder()
                .email(aes256.encrypt(signUpRequest.getEmail()))
                .name(aes256.encrypt(signUpRequest.getName()))
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .phoneNumber(aes256.encrypt(signUpRequest.getPhoneNumber()))
                .address(aes256.encrypt(signUpRequest.getAddress()))
                .role(RoleEnum.USER)
                .build());
    }


    public void sendVerificationCodeEmail(String to) {
        validateDuplicateEmail(to);
        String title = "DEALSHOP 이메일 인증 번호";
        String authCode = this.createEmailVerificationCode();
        log.info(authCode);
        emailVerificationCodeRepository.save(EmailVerificationCode.builder()
                .emailVerificationCode(authCode)
                .email(to)
                .build());
        mailService.sendEmail(to, title, authCode);

    }

    public void validateDuplicateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(aes256.encrypt(email));
        if (user.isPresent()) {
            log.debug("UserService.validateDuplicateEmail exception occur email: {}", email);
            throw new BusinessLogicException(ExceptionCode.USER_EMAIL_EXISTS);
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
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_SUCH_USER));
    }

    public UserInfoResponse getUserInfos(Long userId) {
        User user = findUserByIdOrElseThrow(userId);
        return UserInfoResponse.from(user, aes256);
    }

    @Transactional
    public UpdateUserInfoResponse updateUserInfos(Long userId, UpdateUserInfoRequest updateUserInfoRequest) {
        User user = findUserByIdOrElseThrow(userId);
        user.updateUserInfo(aes256.encrypt(updateUserInfoRequest.getPhoneNumber()), aes256.encrypt(updateUserInfoRequest.getAddress()));
        return new UpdateUserInfoResponse(user, aes256);
    }

    @Transactional
    public void updateUserPassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        User user = findUserByIdOrElseThrow(userId);
        user.updatePassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
    }
}
