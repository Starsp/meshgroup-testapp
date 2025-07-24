package ru.meshgroup.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Email {

    private Long id;
    @NotBlank
    private String email;

}
