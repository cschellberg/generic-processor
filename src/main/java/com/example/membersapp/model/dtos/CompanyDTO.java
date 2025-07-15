package com.example.membersapp.model.dtos;

import com.example.membersapp.entities.Company;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyDTO {
  private Long id; // Primary key
  private String companyName;
  private String city;
  private String state;
  private String pointOfContact;
  private String notes;
  private List<EventDTO> events = new ArrayList<>();

  public CompanyDTO(Company company) {
    this.id = company.getId();
    this.companyName = company.getCompanyName();
    this.city = company.getCity();
    this.state = company.getState();
    this.pointOfContact = company.getPointOfContact();
    this.notes = company.getNotes();
  }
}
