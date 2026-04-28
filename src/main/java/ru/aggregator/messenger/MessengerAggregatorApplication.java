package ru.aggregator.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа Spring Boot.
 * Содержит только main. Вся конфигурация вынесена в отдельные классы.
 */
@SpringBootApplication
public class MessengerAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerAggregatorApplication.class, args);
    }
}
