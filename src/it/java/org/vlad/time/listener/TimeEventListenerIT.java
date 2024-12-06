package org.vlad.time.listener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.google.common.util.concurrent.Uninterruptibles;
import org.vlad.IntegrationTest;
import org.vlad.time.data.MemoryQueue;
import org.vlad.time.job.TimeJobService;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TimeEventListenerIT extends IntegrationTest {

    @Autowired
    private MemoryQueue memoryQueue;

    @Autowired
    private TimeJobService timeJobService;


    @Test
    void onApplicationEvent_when_lost_connection_for_db() {
        ReflectionTestUtils.setField(timeJobService, "jobEnabled", true);

        Uninterruptibles.sleepUninterruptibly(5L, TimeUnit.SECONDS);

        disconnectNetwork();

        Uninterruptibles.sleepUninterruptibly(5L, TimeUnit.SECONDS);

        connectNetwork();

        Uninterruptibles.sleepUninterruptibly(3L, TimeUnit.SECONDS);

        assertThat(memoryQueue.getMemoryQueue().size()).isLessThanOrEqualTo(1);

        ReflectionTestUtils.setField(timeJobService, "jobEnabled", false);
    }


    private void disconnectNetwork() {
        try {
            Runtime.getRuntime().exec("docker network disconnect bridge " + POSTGRESQL_CONTAINER.getContainerId());
        } catch (IOException e) {
            log.info("Can't exec disconnect");
        }
    }


    private void connectNetwork() {
        try {
            Runtime.getRuntime().exec("docker network connect bridge " + POSTGRESQL_CONTAINER.getContainerId());
        } catch (IOException e) {
            log.info("Can't exec connect");
        }
    }
}
