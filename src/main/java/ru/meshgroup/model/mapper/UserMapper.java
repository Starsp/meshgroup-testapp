package ru.meshgroup.model.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.meshgroup.model.request.CreateUserContactRequest;
import ru.meshgroup.model.request.UpdateUserContactRequest;
import ru.meshgroup.model.response.UserInfo;
import ru.meshgroup.persistance.model.Account;
import ru.meshgroup.persistance.model.User;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "account", source = "balance")
    User toEntity(CreateUserContactRequest user);

    UserInfo toDto(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "emailList", source = "emailList")
    @Mapping(target = "phoneList", source = "phoneList")
    User toEntity(@MappingTarget User user, UpdateUserContactRequest updateUserRequest);

    default Account createAccount(BigDecimal balance) {
        Account account = new Account();
        account.setBalance(balance);
        account.setInitialDeposit(balance);
        return account;
    }

    @AfterMapping
    default void setRelations(@MappingTarget User user) {
        CollectionUtils.emptyIfNull(user.getEmailList())
                .forEach(email -> email.setUser(user));

        CollectionUtils.emptyIfNull(user.getPhoneList())
                .forEach(email -> email.setUser(user));

        Optional.ofNullable(user.getAccount()).ifPresent(account -> account.setUser(user));
    }

}
