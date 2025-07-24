package ru.meshgroup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.meshgroup.properties.AccountProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AccountProperties.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
