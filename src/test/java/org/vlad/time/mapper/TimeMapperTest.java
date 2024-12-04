package org.vlad.time.mapper;

import java.time.LocalTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.vlad.time.Time;
import org.vlad.time.TimeMapper;
import org.vlad.time.TimeMapperImpl;

class TimeMapperTest {

    public static final LocalTime TIME = LocalTime.MAX;

    private final TimeMapper mapper = new TimeMapperImpl();


    @Test
    void mapToDto() {
        var entity = new Time()
                .setTime(TIME);

        var result = mapper.mapToDto(entity);

        SoftAssertions.assertSoftly((softly) ->
                softly.assertThat(result.getTime()).isEqualTo(TIME));
    }
}