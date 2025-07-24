package ru.meshgroup.model.response;

import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {

    private List<UserInfo> userList;
    private Long total;

}
