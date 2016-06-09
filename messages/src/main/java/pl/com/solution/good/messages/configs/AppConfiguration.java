package pl.com.solution.good.messages.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfiguration {	
	@Bean
	public ObjectMapper objectMapper() {	
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}
}
