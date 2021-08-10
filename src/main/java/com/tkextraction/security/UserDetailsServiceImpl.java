package com.tkextraction.security;

import com.tkextraction.domain.UserEntity;
import com.tkextraction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUserName(username);
        if(!user.isPresent()) {
            log.info("[x] New user logged in. Name name: {}.", username);
            userRepository.save(new UserEntity(username));
        }
        return new CustomUserDetails(username, "password");
    }
}
