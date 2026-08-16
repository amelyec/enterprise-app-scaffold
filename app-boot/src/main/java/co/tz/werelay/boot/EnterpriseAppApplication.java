package co.tz.werelay.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.tz.werelay")
@EntityScan("co.tz.werelay.persistence.entity")
@EnableJpaRepositories("co.tz.werelay.persistence.repository")
public class EnterpriseAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseAppApplication.class, args);
    }
}
