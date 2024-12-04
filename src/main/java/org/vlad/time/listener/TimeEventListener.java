package org.vlad.time.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.vlad.time.TimeService;
import org.vlad.time.data.MemoryQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeEventListener implements ApplicationListener<TimeEvent> {

    private final MemoryQueue memoryQueue;

    private final TimeService timeService;


    @Async
    @Override
    public void onApplicationEvent(TimeEvent event) {
        try {
            while (!memoryQueue.getMemoryQueue().isEmpty()) {
                timeService.saveAndFlush(memoryQueue.getMemoryQueue().peek());
                memoryQueue.getMemoryQueue().poll();
            }
        } catch (Exception e) {
            log.warn("Connection exception = {}", e.toString());
        }
    }
}
