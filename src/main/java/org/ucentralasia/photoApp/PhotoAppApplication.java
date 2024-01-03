package org.ucentralasia.photoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PhotoAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SpringAppContext springApplicationContext() {
        return new SpringAppContext();
    }
}
