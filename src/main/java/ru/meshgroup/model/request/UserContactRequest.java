package ru.meshgroup.model.request;

import lombok.Data;
import ru.meshgroup.model.Email;
import ru.meshgroup.model.Phone;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserContactRequest {

    @NotEmpty
    private List<@Valid Phone> phoneList;
    @NotEmpty
    private List<@Valid Email> emailList;

}
