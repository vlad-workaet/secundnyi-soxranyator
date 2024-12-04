package org.vlad.time;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.vlad.time.dto.TimeDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TimeMapper {

    TimeDto mapToDto(Time entity);


    List<TimeDto> mapListToDto(List<Time> entities);

}
