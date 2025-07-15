package com.example.membersapp.controller;

import com.example.membersapp.entities.Company;
import com.example.membersapp.model.dtos.CompanyDTO;
import com.example.membersapp.model.dtos.CompanyMapper;
import com.example.membersapp.repository.CompanyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

  @Autowired private CompanyRepository companyRepository;
  @Autowired private CompanyMapper companyMapper;

  @GetMapping
  public List<CompanyDTO> getAllCompanies() {
    return companyMapper.toDtoList(companyRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
    var optionalCompany = companyRepository.findById(id);
    return optionalCompany
        .map(company -> ResponseEntity.ok(companyMapper.toDto(company)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
    return companyMapper.toDto(companyRepository.save(companyMapper.toEntity(companyDTO)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CompanyDTO> updateCompany(
      @PathVariable Long id, @RequestBody CompanyDTO companyDetails) {
    var optionalCompany = companyRepository.findById(id);
    if (optionalCompany.isPresent()) {
      var company = optionalCompany.get();
      company.setCompanyName(companyDetails.getCompanyName());
      company.setCity(companyDetails.getCity());
      company.setState(companyDetails.getState());
      company.setPointOfContact(companyDetails.getPointOfContact());
      company.setNotes(companyDetails.getNotes());
      Company updatedCompany = companyRepository.save(company);
      return ResponseEntity.ok(companyMapper.toDto(updatedCompany));
    } else {
      return ResponseEntity.notFound().build();
    }
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
