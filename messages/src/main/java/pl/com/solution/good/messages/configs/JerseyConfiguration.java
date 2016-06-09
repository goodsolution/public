package pl.com.solution.good.messages.configs;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import pl.com.solution.good.messages.controllers.MessageController;

@Configuration
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        register(MessageController.class);
    }
}
