package ru.meshgroup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.meshgroup.exception.AuthenticationException;
import ru.meshgroup.model.request.AuthenticationRequest;
import ru.meshgroup.persistance.repository.UserRepository;
import ru.meshgroup.security.JwtTokenUtil;
import ru.meshgroup.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(AuthenticationRequest request) {
        return userRepository.findUserByPhone(request.getPhone())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> jwtTokenUtil.generateToken(user.getId()))
                .orElseThrow(AuthenticationException::new);

    }
}
