package com.giovanetti;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApplication {

    @Bean
    public ResourceConfig jerseyConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        resourceConfig.packages(RestApplication.class.getPackage().getName());
        return resourceConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

}
