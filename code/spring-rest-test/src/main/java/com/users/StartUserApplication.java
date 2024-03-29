package com.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import com.users.domain.User;

@SpringBootApplication
public class StartUserApplication {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartUserApplication.class, args);
    }
    
    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {        	
        	repository.save(new User("Nom1", "one@email.com",21, "m", true));
        	repository.save(new User("Nom2", "two@email.com",22, "w", true));
        	repository.save(new User("Nom3", "one@email.com",23, "w", true));
        };
    }

}