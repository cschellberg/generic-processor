package com.example.membersapp.model;

import jakarta.persistence.*;
import java.time.LocalDate; // For date without time
// For date and time
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

  @ManyToOne(fetch = FetchType.LAZY) // Many events to one company
  @JoinColumn(
      name = "company_id",
      nullable = false) // This is the foreign key column in the 'events' table
  private Company company;
}
