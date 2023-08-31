package ru.koryakin.diplomproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.koryakin.diplomproject.config.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableJpaRepositories("ru.koryakin.diplomproject.repository")
@SpringBootApplication
public class DiplomProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomProjectApplication.class, args);
    }
}
