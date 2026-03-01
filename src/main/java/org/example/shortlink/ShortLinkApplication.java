package org.example.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShortLinkApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ShortLinkApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
