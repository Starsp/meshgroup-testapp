package ru.meshgroup.persistance.repository;

import org.springframework.data.domain.Page;
import ru.meshgroup.model.request.GetUserListRequest;
import ru.meshgroup.persistance.model.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Page<User> getUserList(GetUserListRequest request);

    Optional<User> findUserByPhone(String phone);

}
