package com.hendisantika.microservice.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The Main Spring Boot Application class.<br>
 * <br>
 * <p>
 * The {@link EnableConfigServer} annotation defines that this application will
 * serve as the REST based API for providing external configuration. <br>
 * <br>
 * <p>
 * The external repository from where the configuration will be picked up is
 * defined in the {@linkplain application.yml} file.
 *
 * @author hendisantika
 */

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
