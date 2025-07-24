package ru.meshgroup.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.meshgroup.TestApplication;
import ru.meshgroup.model.Email;
import ru.meshgroup.model.Phone;
import ru.meshgroup.model.request.AuthenticationRequest;
import ru.meshgroup.model.request.CreateUserContactRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApplication.class)
public abstract class BaseTest {

    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD)
            .withReuse(true);

    @DynamicPropertySource
    static void wireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    protected CreateUserContactRequest createUserRequest(String phoneNumber, BigDecimal balance) {
        CreateUserContactRequest createUserRequest = new CreateUserContactRequest();
        createUserRequest.setBalance(balance);
        createUserRequest.setName("TEST");
        createUserRequest.setPassword("TEST");
        createUserRequest.setDateOfBirth(LocalDate.now().minusYears(30));

        Phone phone = new Phone();
        phone.setPhone(phoneNumber);
        createUserRequest.setPhoneList(List.of(phone));

        Email email = new Email();
        email.setEmail(phoneNumber + "@email.com");
        createUserRequest.setEmailList(List.of(email));

        return createUserRequest;
    }

    @SneakyThrows
    protected String getToken(String phone) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPhone(phone);
        authenticationRequest.setPassword("TEST");

        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
    }

}
