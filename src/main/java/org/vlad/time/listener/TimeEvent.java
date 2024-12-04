package org.vlad.time.listener;

import org.springframework.context.ApplicationEvent;

public class TimeEvent extends ApplicationEvent {

    public TimeEvent(Object source) {
        super(source);
    }
}
