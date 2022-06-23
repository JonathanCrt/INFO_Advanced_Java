package com.training.crete.springbootTest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SpringbootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(EventRepository repository) {
        return args -> {
            Event event = new Event(
                    "Exposition couleurs",
                    Tag.EXPO,
                    "22-09-2022",
                    new Address("France", 34000, "Montpellier"),
                    new Date(),
                    List.of(43.82, 15.20),
                    true,
                    BigDecimal.TEN
            );
			Event event2 = new Event(
					"Exposition du Voyage",
					Tag.EXPO,
					"24-09-2022",
					new Address("France", 34000, "Montpellier"),
					new Date(),
					List.of(43.82, 17.20),
					true,
					BigDecimal.ONE
			);
            repository.insert(event);
			repository.insert(event2);
        };


    }
}
