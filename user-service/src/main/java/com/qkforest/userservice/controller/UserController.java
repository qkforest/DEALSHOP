package com.qkforest.userservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.userservice.dto.request.SignUpRequest;
import com.qkforest.userservice.dto.request.UpdatePasswordRequest;
import com.qkforest.userservice.dto.request.UpdateUserInfoRequest;
import com.qkforest.userservice.dto.response.UpdateUserInfoResponse;
import com.qkforest.userservice.dto.response.UserInfoResponse;
import com.qkforest.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "회원가입에 성공했습니다.", null), HttpStatus.CREATED);
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> getUserInfos(@RequestHeader("X_User_Id") Long userId) {
        UserInfoResponse userDetails = userService.getUserInfos(userId);
        return new ResponseEntity<>(new ResponseDto<>(200, "회원 정보 조회에 성공했습니다.", userDetails), HttpStatus.OK);
    }

    @PatchMapping ("/auth/users")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("X_User_Id") Long userId,
                                               @RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        UpdateUserInfoResponse updateUserInfoResponse = userService.updateUserInfos(userId, updateUserInfoRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "회원 정보 수정에 성공했습니다.", updateUserInfoResponse), HttpStatus.OK);
    }

    @PatchMapping("/auth/users/password")
    public ResponseEntity<?> updateUserPassword(@RequestHeader("X_User_Id") Long userId,
                                                @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        userService.updateUserPassword(userId, updatePasswordRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "패스워드 수정에 성공했습니다.", null), HttpStatus.OK);
    }
}
