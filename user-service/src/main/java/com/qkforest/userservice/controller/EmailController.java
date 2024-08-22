package com.qkforest.userservice.controller;

import com.qkforest.userservice.service.UserService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final UserService userService;

    @PostMapping("/email/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid @Email String email) {
        userService.sendVerificationCodeEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
