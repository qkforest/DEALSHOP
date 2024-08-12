package com.qkforest.userservice.controller;

import com.qkforest.userservice.dto.request.SignUpRequest;
import com.qkforest.userservice.dto.response.ResponseDto;
import com.qkforest.userservice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")

public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        System.out.println(signUpRequest);
        memberService.createMember(signUpRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입에 성공했습니다.", null), HttpStatus.CREATED);
    }
}
