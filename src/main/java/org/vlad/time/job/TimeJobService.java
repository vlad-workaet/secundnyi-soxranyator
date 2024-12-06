package org.vlad.time.job;

import java.time.LocalTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.vlad.time.Time;
import org.vlad.time.data.MemoryQueue;
import org.vlad.time.listener.TimeEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeJobService {

    public static final int FIXED_RATE_MS = 1000;

    private final MemoryQueue memoryQueue;

    private final ApplicationEventPublisher publisher;

    @Value("${app.time.job-enabled}")
    private Boolean jobEnabled;


    @Scheduled(fixedRate = FIXED_RATE_MS)
    public void saveTime() {
        if (jobEnabled) {
            memoryQueue.getMemoryQueue()
                    .add(new Time()
                            .setTime(LocalTime.now()));

            log.info("Add new time. Count = {}", memoryQueue.getMemoryQueue().size());

            publisher.publishEvent(new TimeEvent(this));
        }
    }
}
