package ru.meshgroup.integration.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.meshgroup.integration.BaseTest;
import ru.meshgroup.model.request.CreateUserContactRequest;
import ru.meshgroup.persistance.repository.UserRepository;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends BaseTest {

    private static final String USER_PHONE = "1231231231231";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    void createUserTest() {
        CreateUserContactRequest userRequest = createUserRequest(USER_PHONE, BigDecimal.valueOf(100));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status()
                        .isCreated());
    }

    @Test
    @SneakyThrows
    void getUserListTest() {
        createUserTest();

        String token = getToken(USER_PHONE);

        mockMvc.perform(get("/user/list")
                        .queryParam("phone", USER_PHONE)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList.length()").value(1));
    }

}
