package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class UniversityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
    }

    @Bean
    public OpenAPI universityAPI() {
        return new OpenAPI()
                .info(new Info().title("University API")
                        .description("Apllication for managing your univesity")
                        .version("1.00.00")
                        .contact(new Contact()
                                .name("Stanislav Zverev")
                                .email("stanislav.zv@yandex.ru")));
    }

}