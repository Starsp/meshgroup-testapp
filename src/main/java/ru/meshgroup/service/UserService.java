package ru.meshgroup.service;

import ru.meshgroup.model.request.CreateUserContactRequest;
import ru.meshgroup.model.request.GetUserListRequest;
import ru.meshgroup.model.request.UpdateUserContactRequest;
import ru.meshgroup.model.response.UserInfo;
import ru.meshgroup.model.response.UserListResponse;

public interface UserService {

    void createUser(CreateUserContactRequest createUserRequest);

    void updateUser(UpdateUserContactRequest updateUserRequest);

    UserInfo getUserInfo(Long id);

    UserListResponse getUserList(GetUserListRequest request);

}
