package org.vlad.time;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.util.concurrent.Uninterruptibles;
import org.vlad.IntegrationTest;
import org.vlad.time.data.MemoryQueue;
import org.vlad.time.dto.TimeDto;
import org.vlad.time.job.TimeJobService;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseSetup(value = "classpath:dbunit/time.xml")
public class TimeControllerIT extends IntegrationTest {

    private static final String URL_TEMPLATE = "local/api/v1/time";

    @Autowired
    private WebTestClient client;

    @Autowired
    private TimeJobService timeJobService;

    @Autowired
    private MemoryQueue memoryQueue;


    @Test
    void getAll_smoke() {
        client.get()
                .uri(URL_TEMPLATE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<List<TimeDto>>() {
                })
                .value((response) ->
                        SoftAssertions.assertSoftly((softly) -> {
                            softly.assertThat(response.get(0).getTime()).isEqualTo(LocalTime.of(10, 10, 0));
                            softly.assertThat(response.get(1).getTime()).isEqualTo(LocalTime.of(10, 10, 1));
                            softly.assertThat(response.get(2).getTime()).isEqualTo(LocalTime.of(10, 10, 2));
                            softly.assertThat(response.get(3).getTime()).isEqualTo(LocalTime.of(10, 10, 3));
                            softly.assertThat(response.get(4).getTime()).isEqualTo(LocalTime.of(10, 10, 4));
                        }));
    }


    @Test
    void getAll_with_enabled_job() {
        ReflectionTestUtils.setField(timeJobService, "jobEnabled", true);

        Uninterruptibles.sleepUninterruptibly(5L, TimeUnit.SECONDS);

        client.get()
                .uri(URL_TEMPLATE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<List<TimeDto>>() {
                })
                .value((response) ->
                        SoftAssertions.assertSoftly((softly) -> {
                            softly.assertThat(response.size()).isGreaterThanOrEqualTo(9);
                        }));

        ReflectionTestUtils.setField(timeJobService, "jobEnabled", false);

        Uninterruptibles.sleepUninterruptibly(2L, TimeUnit.SECONDS);

        assertThat(memoryQueue.getMemoryQueue().size()).isEqualTo(0);
    }
}
