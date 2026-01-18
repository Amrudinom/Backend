package com.dke.foerderportal.antragstellung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {
        "com.dke.foerderportal.antragstellung",
        "com.dke.foerderportal.shared"
})

@EntityScan("com.dke.foerderportal.shared.model")
@EnableJpaRepositories("com.dke.foerderportal.shared.repository")
public class AntragsstellungApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntragsstellungApplication.class, args);
    }

}
@RestController
@RequestMapping("/api/_whoami")
class WhoAmIController {
    @GetMapping
    public String who() { return "antragstellung"; }
}