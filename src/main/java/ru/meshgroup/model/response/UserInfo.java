package ru.meshgroup.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.meshgroup.model.Email;
import ru.meshgroup.model.Phone;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserInfo {

    private Long id;
    private String name;
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;
    private List<Email> emailList;
    private List<Phone> phoneList;

}
