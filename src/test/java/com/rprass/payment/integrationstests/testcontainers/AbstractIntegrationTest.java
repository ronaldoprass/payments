package com.rprass.payment.integrationstests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:10");

        private static void startContainers() {
            Startables.deepStart(Stream.of(database)).join();
        }

        private static Map<String, String> createConnectionConfiguratio() {
            return Map.of(
                    "spring.datasource.url", database.getJdbcUrl(),
                    "spring.datasource.username", database.getUsername(),
                    "spring.datasource.password", database.getPassword()
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguratio());
            environment.getPropertySources().addFirst(testContainers);
        }


    }
}