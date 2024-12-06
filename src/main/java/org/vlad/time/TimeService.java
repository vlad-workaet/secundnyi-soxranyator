package org.vlad.time;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlad.time.dto.TimeDto;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository repository;

    private final TimeMapper mapper;


    @Transactional(readOnly = true)
    public List<TimeDto> getAll() {
        return mapper.mapListToDto(repository.findAll());
    }


    @Transactional
    public Time saveAndFlush(Time entity) {
        return repository.saveAndFlush(entity);
    }
}
