package com.qkforest.userservice.security;

import com.qkforest.userservice.domain.User;
import com.qkforest.userservice.repositroy.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.qkforest.userservice.util.AES256;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AES256 aes256;

    public PrincipalDetailsService(UserRepository userRepository, AES256 aes256) {
        this.userRepository = userRepository;
        this.aes256 = aes256;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(aes256.encrypt(username))
                .orElseThrow(() -> new InternalAuthenticationServiceException("인증 실패"));
        return new PrincipalDetails(user);
    }
}
