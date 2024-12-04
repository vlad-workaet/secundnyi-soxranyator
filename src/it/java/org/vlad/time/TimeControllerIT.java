package org.vlad.time;

import java.time.LocalTime;
import java.util.List;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.vlad.IntegrationTest;
import org.vlad.time.dto.TimeDto;

@DatabaseSetup(value = "classpath:dbunit/time.xml")
public class TimeControllerIT extends IntegrationTest {

    private static final String URL_TEMPLATE = "local/api/v1/time";

    @Autowired
    private WebTestClient client;


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
}
