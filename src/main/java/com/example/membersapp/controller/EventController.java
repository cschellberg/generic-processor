package com.example.membersapp.controller;

import com.example.membersapp.model.dtos.CompanyMapper;
import com.example.membersapp.model.dtos.EventDTO;
import com.example.membersapp.model.dtos.EventMapper;
import com.example.membersapp.repository.CompanyRepository;
import com.example.membersapp.repository.EventRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Or /api/events, but /api/companies/{companyId}/events might be better
public class EventController {

  @Autowired private EventRepository eventRepository;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private EventMapper eventMapper;

  @Autowired private CompanyMapper companyMapper;

  // Get all events for a specific company
  @GetMapping("/companies/{companyId}/events")
  public ResponseEntity<List<EventDTO>> getEventsByCompanyId(@PathVariable Long companyId) {
    if (!companyRepository.existsById(companyId)) {
      return ResponseEntity.notFound().build();
    }
    List<EventDTO> events = eventMapper.toDtoList(eventRepository.findByCompanyId(companyId));
    return ResponseEntity.ok(events);
  }

  @GetMapping("/events")
  public ResponseEntity<List<EventDTO>> getEvents() {
    List<EventDTO> events = eventMapper.toDtoList(eventRepository.findAll());
    return ResponseEntity.ok(events);
  }

  // Get a specific event
  @GetMapping("/events/{id}")
  public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
    var optionalEvent = eventRepository.findById(id);
    return optionalEvent
        .map(event -> ResponseEntity.ok(eventMapper.toDto(event)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Create an event for a specific company
  @PostMapping("/companies/{companyId}/events")
  public ResponseEntity<EventDTO> createEvent(
      @PathVariable Long companyId, @RequestBody EventDTO event) {
    return companyRepository
        .findById(companyId)
        .map(
            company -> {
              event.setCompany(companyMapper.toDto(company));
              EventDTO createdEvent =
                  eventMapper.toDto(eventRepository.save(eventMapper.toEntity(event)));
              return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
            })
        .orElse(ResponseEntity.notFound().build());
  }

  // Update an event
  @PutMapping("/events/{id}")
  public ResponseEntity<EventDTO> updateEvent(
      @PathVariable Long id, @RequestBody EventDTO eventDetails) {
    var optionalEvent = eventRepository.findById(id);
    if (optionalEvent.isPresent()) {
      var event = optionalEvent.get();
      event.setDate(eventDetails.getDate());
      event.setType(eventDetails.getType());
      event.setAction(eventDetails.getAction());
      EventDTO updatedEvent = eventMapper.toDto(eventRepository.save(event));
      return ResponseEntity.ok(updatedEvent);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // Delete an event
  @DeleteMapping("/events/{id}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    return eventRepository
        .findById(id)
        .map(
            event -> {
              eventRepository.delete(event);
              return ResponseEntity.ok().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
