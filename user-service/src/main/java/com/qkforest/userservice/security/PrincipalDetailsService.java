package com.qkforest.userservice.security;

import com.qkforest.userservice.domain.Member;
import com.qkforest.userservice.repositroy.MemberRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.qkforest.userservice.util.AES256;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final AES256 aes256;

    public PrincipalDetailsService(MemberRepository userRepository, AES256 aes256) {
        this.memberRepository = userRepository;
        this.aes256 = aes256;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(aes256.encrypt(username))
                .orElseThrow(() -> new InternalAuthenticationServiceException("인증 실패"));
        return new PrincipalDetails(member);
    }
}
