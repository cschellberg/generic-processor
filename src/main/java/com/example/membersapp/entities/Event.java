package com.example.membersapp.entities;

import com.example.membersapp.model.dtos.EventDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate; // For date without time
// For date and time
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_date", nullable = false)
  private LocalDate date; // Or LocalDateTime if you need time as well

  @Column(name = "event_type", nullable = false)
  private String type;

  @Column(name = "action_taken", columnDefinition = "TEXT")
  private String action;

  @ToString.Exclude
  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY) // Many events to one company
  @JoinColumn(
      name = "company_id",
      nullable = false) // This is the foreign key column in the 'events' table
  private Company company;

  public Event(EventDTO eventDTO) {
    this.id = eventDTO.getId();
    this.date = eventDTO.getDate();
    this.type = eventDTO.getType();
    this.action = eventDTO.getAction();
    this.company = new Company(eventDTO.getCompany());
  }
}
