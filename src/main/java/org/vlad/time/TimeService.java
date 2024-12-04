package org.vlad.time;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository repository;


    @Transactional(readOnly = true)
    public List<Time> getAll() {
        return repository.findAll();
    }


    @Transactional
    public Time saveAndFlush(Time entity) {
        return repository.saveAndFlush(entity);
    }
}
