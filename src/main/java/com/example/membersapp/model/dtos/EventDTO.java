package com.example.membersapp.model.dtos;

import com.example.membersapp.entities.Event;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDTO {
  private Long id;
  private LocalDate date;
  private String type;
  private String action;
  private CompanyDTO company;

  public EventDTO(Event event) {
    this.id = event.getId();
    this.date = event.getDate();
    this.type = event.getType();
    this.action = event.getAction();
    var company = event.getCompany();
    this.company =
        new CompanyDTO(
            company.getId(),
            company.getCompanyName(),
            company.getCity(),
            company.getState(),
            company.getPointOfContact(),
            company.getNotes(),
            null);
  }
}
