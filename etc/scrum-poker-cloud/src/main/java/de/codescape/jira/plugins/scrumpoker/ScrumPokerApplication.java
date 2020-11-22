package de.codescape.jira.plugins.scrumpoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrumPokerApplication {

    public static void main(String[] args) {
        new SpringApplication(ScrumPokerApplication.class).run(args);
    }

}
