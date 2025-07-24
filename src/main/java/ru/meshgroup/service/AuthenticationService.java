package ru.meshgroup.service;

import ru.meshgroup.model.request.AuthenticationRequest;

public interface AuthenticationService {

    String login(AuthenticationRequest request);

}
