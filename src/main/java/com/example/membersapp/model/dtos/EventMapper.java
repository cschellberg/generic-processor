package com.example.membersapp.model.dtos;

import com.example.membersapp.entities.Event;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
  public EventDTO toDto(Event event) {
    return new EventDTO(event);
  }

  // You can also define the reverse mapping if needed
  public Event toEntity(EventDTO eventDTO) {
    return new Event(eventDTO);
  }

  public List<EventDTO> toDtoList(List<Event> events) {
    List<EventDTO> dtos = new ArrayList<>();
    events.forEach(c -> dtos.add(toDto(c)));
    return dtos;
  }

  public List<Event> toEntityList(List<EventDTO> eventDTOs) {
    List<Event> entityList = new ArrayList<>();
    eventDTOs.forEach(c -> entityList.add(toEntity(c)));
    return entityList;
  }
}
