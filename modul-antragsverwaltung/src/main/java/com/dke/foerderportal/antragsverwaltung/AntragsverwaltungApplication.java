package com.dke.foerderportal.antragsverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.dke.foerderportal.antragsverwaltung",
        "com.dke.foerderportal.shared"
})

@EntityScan("com.dke.foerderportal.shared.model")
@EnableJpaRepositories("com.dke.foerderportal.shared.repository")
public class AntragsverwaltungApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntragsverwaltungApplication.class, args);
    }

}