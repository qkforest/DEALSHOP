package com.qkforest.userservice.controller;

import com.qkforest.userservice.dto.request.SignUpRequest;
import com.qkforest.userservice.dto.response.MemberDetailResponse;
import com.qkforest.userservice.dto.response.ResponseDto;
import com.qkforest.userservice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        System.out.println(signUpRequest);
        memberService.createMember(signUpRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입에 성공했습니다.", null), HttpStatus.CREATED);
    }

    @GetMapping("/auth/members")
    public ResponseEntity<?> getMemberDetails(@RequestHeader("x-member-id") Long memberId) {
        MemberDetailResponse memberDetails = memberService.getMemberDetails(memberId);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보 조회에 성공했습니다.", memberDetails), HttpStatus.OK);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 되었습니다.", null), HttpStatus.OK);
    }
}
