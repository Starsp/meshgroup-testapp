package ru.meshgroup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.meshgroup.model.request.CreateUserContactRequest;
import ru.meshgroup.model.request.GetUserListRequest;
import ru.meshgroup.model.request.UpdateUserContactRequest;
import ru.meshgroup.model.response.UserInfo;
import ru.meshgroup.model.response.UserListResponse;
import ru.meshgroup.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserContactRequest request) {
        userService.createUser(request);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@Valid @RequestBody UpdateUserContactRequest request) {
        userService.updateUser(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserInfo getUserIngo(@PathVariable Long id) {
        return userService.getUserInfo(id);
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public UserListResponse getUserList(GetUserListRequest request) {
        return userService.getUserList(request);
    }

}
