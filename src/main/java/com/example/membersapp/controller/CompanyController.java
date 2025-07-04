package com.example.membersapp.controller;

import com.example.membersapp.model.Company;
import com.example.membersapp.repository.CompanyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

  @Autowired private CompanyRepository companyRepository;

  @GetMapping
  public List<Company> getAllCompanies() {
    return companyRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
    return companyRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Company createCompany(@RequestBody Company company) {
    return companyRepository.save(company);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Company> updateCompany(
      @PathVariable Long id, @RequestBody Company companyDetails) {
    return companyRepository
        .findById(id)
        .map(
            company -> {
              company.setCompanyName(companyDetails.getCompanyName());
              company.setCity(companyDetails.getCity());
              company.setState(companyDetails.getState());
              company.setPointOfContact(companyDetails.getPointOfContact());
              company.setNotes(companyDetails.getNotes());
              Company updatedCompany = companyRepository.save(company);
              return ResponseEntity.ok(updatedCompany);
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
    return companyRepository
        .findById(id)
        .map(
            company -> {
              companyRepository.delete(company);
              return ResponseEntity.ok().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
