package ru.koryakin.diplomproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.koryakin.diplomproject.config.RsaKeyProperties;

@Slf4j
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableJpaRepositories("ru.koryakin.diplomproject.repository")
@SpringBootApplication
public class DiplomProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomProjectApplication.class, args);
        log.info("Application is up");
    }
}
