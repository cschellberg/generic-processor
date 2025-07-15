package com.example.membersapp.entities;

import com.example.membersapp.model.dtos.CompanyDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies") // It's good practice to pluralize table names
@Data // Lombok annotation for getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok annotation for no-arg constructor
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // Primary key

  @Column(name = "company_name", nullable = false)
  private String companyName;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "point_of_contact")
  private String pointOfContact;

  @Column(name = "notes", columnDefinition = "TEXT") // Use TEXT for potentially long notes
  private String notes;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "company",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  // 'mappedBy' indicates that the 'company' field in the Event entity owns the relationship.
  // CascadeType.ALL: Operations (persist, merge, remove) on Company will cascade to associated
  // Events.
  // orphanRemoval: If an Event is removed from the company's events list, it will be deleted from
  // the database.
  // FetchType.LAZY: Events will be loaded only when explicitly accessed.
  private List<Event> events = new ArrayList<>();

  // Helper methods to manage the bidirectional relationship
  public void addEvent(Event event) {
    events.add(event);
    event.setCompany(this);
  }

  public void removeEvent(Event event) {
    events.remove(event);
    event.setCompany(null);
  }

  public Company(CompanyDTO companyDTO) {
    this.companyName = companyDTO.getCompanyName();
    this.city = companyDTO.getCity();
    this.state = companyDTO.getState();
    this.pointOfContact = companyDTO.getPointOfContact();
    this.notes = companyDTO.getNotes();
    this.id = companyDTO.getId();
    companyDTO.getEvents().forEach(eventDTO -> events.add(new Event(eventDTO)));
  }
}
