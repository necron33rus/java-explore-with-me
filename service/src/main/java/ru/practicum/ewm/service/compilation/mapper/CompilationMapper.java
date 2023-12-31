package ru.practicum.ewm.service.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.service.compilation.model.Compilation;
import ru.practicum.ewm.service.event.mapper.EventMapper;
import ru.practicum.ewm.service.event.model.Event;

import java.util.List;

@Mapper(uses = EventMapper.class)
public interface CompilationMapper {
    CompilationMapper COMPILATION_MAPPER = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    Compilation fromDto(NewCompilationDto dto, List<Event> events);

    CompilationDto toDto(Compilation compilation);
}
