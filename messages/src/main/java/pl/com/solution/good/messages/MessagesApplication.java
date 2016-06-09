package pl.com.solution.good.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("pl.com.solution.good.messages")
@EnableJpaRepositories("pl.com.solution.good.messages.repositories")
@EnableScheduling
public class MessagesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagesApplication.class, args);
	}
}
