package ru.meshgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.meshgroup.exception.UserNotFoundException;
import ru.meshgroup.model.mapper.UserMapper;
import ru.meshgroup.model.request.CreateUserContactRequest;
import ru.meshgroup.model.request.GetUserListRequest;
import ru.meshgroup.model.request.UpdateUserContactRequest;
import ru.meshgroup.model.response.UserInfo;
import ru.meshgroup.model.response.UserListResponse;
import ru.meshgroup.persistance.model.User;
import ru.meshgroup.persistance.repository.UserRepository;
import ru.meshgroup.security.JwtTokenUtil;
import ru.meshgroup.service.UserService;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void createUser(CreateUserContactRequest createUserRequest) {
        User userEntity = mapper.toEntity(createUserRequest);
        String password = passwordEncoder.encode(createUserRequest.getPassword());
        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(UpdateUserContactRequest request) {
        Long currentUserID = jwtTokenUtil.extractUserIdJwt();
        User user = userRepository.findById(currentUserID)
                .orElseThrow(() -> new UserNotFoundException(currentUserID));
        userRepository.save(mapper.toEntity(user, request));
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserListResponse getUserList(GetUserListRequest request) {
        Page<User> userList = userRepository.getUserList(request);
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setTotal(userList.getTotalElements());
        userListResponse.setUserList(userList.get().map(mapper::toDto).collect(Collectors.toList()));
        return userListResponse;
    }
}
