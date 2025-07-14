package com.example.membersapp.controller;

import com.example.membersapp.model.Event;
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

  // Get all events for a specific company
  @GetMapping("/companies/{companyId}/events")
  public ResponseEntity<List<Event>> getEventsByCompanyId(@PathVariable Long companyId) {
    if (!companyRepository.existsById(companyId)) {
      return ResponseEntity.notFound().build();
    }
    List<Event> events = eventRepository.findByCompanyId(companyId);
    return ResponseEntity.ok(events);
  }

  @GetMapping("/events")
  public ResponseEntity<List<Event>> getEvents() {
    List<Event> events = eventRepository.findAll();
    return ResponseEntity.ok(events);
  }

  // Get a specific event
  @GetMapping("/events/{id}")
  public ResponseEntity<Event> getEventById(@PathVariable Long id) {
    return eventRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Create an event for a specific company
  @PostMapping("/companies/{companyId}/events")
  public ResponseEntity<Event> createEvent(@PathVariable Long companyId, @RequestBody Event event) {
    return companyRepository
        .findById(companyId)
        .map(
            company -> {
              event.setCompany(company);
              Event createdEvent = eventRepository.save(event);
              return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
            })
        .orElse(ResponseEntity.notFound().build());
  }

  // Update an event
  @PutMapping("/events/{id}")
  public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
    return eventRepository
        .findById(id)
        .map(
            event -> {
              event.setDate(eventDetails.getDate());
              event.setType(eventDetails.getType());
              event.setAction(eventDetails.getAction());
              // You might allow changing the associated company, but for simplicity, we'll assume
              // it's fixed.
              // If you need to change the company, you'd need to fetch the new company and set it.
              Event updatedEvent = eventRepository.save(event);
              return ResponseEntity.ok(updatedEvent);
            })
        .orElse(ResponseEntity.notFound().build());
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
