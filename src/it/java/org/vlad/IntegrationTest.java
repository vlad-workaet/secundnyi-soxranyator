package org.vlad;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureWebTestClient
@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        value = {DbUnitTestExecutionListener.class})
public abstract class IntegrationTest {

    public static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11.2")
            .withCreateContainerCmdModifier(
                    cmd -> cmd.withHostConfig(
                            new HostConfig().withPortBindings(
                                    new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))))
            .withReuse(true);


    @SneakyThrows
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(POSTGRESQL_CONTAINER).join();

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }


    @AfterEach
    void afterEach(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("alter sequence time_id_seq restart");

        jdbcTemplate.execute("delete from time where id > -1");
    }
}

