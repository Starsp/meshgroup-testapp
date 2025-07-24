package ru.meshgroup.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Phone {

    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 13, max = 13)
    private String phone;

}
