package ru.meshgroup.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequest {

    @NotBlank
    private String phone;
    @NotBlank
    private String password;

}
