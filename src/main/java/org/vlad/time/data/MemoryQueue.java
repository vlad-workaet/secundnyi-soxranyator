package org.vlad.time.data;

import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vlad.time.Time;

@Getter
@Component
public class MemoryQueue {

    private ConcurrentLinkedQueue<Time> memoryQueue = new ConcurrentLinkedQueue<>();
}
