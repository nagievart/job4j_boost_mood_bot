package ru.job4j.bmb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Устанавливаем прокси ДО запуска Spring
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "9150");
        System.setProperty("java.net.preferIPv4Stack", "true");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        System.out.println("Приложение успешно запущено. Бот работает...");
    }
}