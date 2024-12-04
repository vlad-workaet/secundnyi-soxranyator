package org.vlad.time;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vlad.time.dto.TimeDto;

@RestController
@RequestMapping(path = "local/api/v1/time")
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;


    @GetMapping
    public List<TimeDto> getAll() {
        return timeService.getAll();
    }
}
