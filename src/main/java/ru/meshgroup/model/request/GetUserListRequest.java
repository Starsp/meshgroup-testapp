package ru.meshgroup.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetUserListRequest {

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    private String name;
    private String phone;
    private String email;

    private Integer page;
    private Integer size;

}
