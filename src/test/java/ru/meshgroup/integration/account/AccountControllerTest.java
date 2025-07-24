package ru.meshgroup.integration.account;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.meshgroup.integration.BaseTest;
import ru.meshgroup.model.request.BalanceTransferRequest;
import ru.meshgroup.persistance.model.User;
import ru.meshgroup.persistance.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AccountControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void testConcurrentTransfers() {
        String firstPhone = "1231231231231";
        String secondPhone = "1231231231232";
        BigDecimal balance = BigDecimal.valueOf(100, 4);
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest(firstPhone, balance))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest(secondPhone, balance))))
                .andExpect(status().isCreated());

        List<Long> userIdList = userRepository.findAll().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        Assertions.assertEquals(2, userIdList.size());

        IntStream.range(0, balance.intValue()).parallel().forEach(i -> {
            try {
                boolean isFirstUser = i % 2 == 0;
                BalanceTransferRequest balanceTransferRequest = new BalanceTransferRequest();
                balanceTransferRequest.setAmount(BigDecimal.ONE);
                balanceTransferRequest.setRecipientId(userIdList.get(isFirstUser ? 0 : 1));

                String token = getToken(isFirstUser ? secondPhone : firstPhone);

                mockMvc.perform(put("/account/transfer")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(balanceTransferRequest)))
                        .andExpect(status().isOk());
            } catch (Exception ignore) {
            }
        });

        List<User> userList = userRepository.findAll();
        Assertions.assertEquals(2, userList.size());

        for (User user : userList) {
            Assertions.assertEquals(balance, user.getAccount().getBalance());
        }
    }

}
