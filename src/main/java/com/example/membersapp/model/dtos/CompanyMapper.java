package com.example.membersapp.model.dtos;

import com.example.membersapp.entities.Company;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

  public CompanyDTO toDto(Company company) {
    return new CompanyDTO(company);
  }

  // You can also define the reverse mapping if needed
  public Company toEntity(CompanyDTO companyDTO) {
    return new Company(companyDTO);
  }

  public List<CompanyDTO> toDtoList(List<Company> companies) {
    List<CompanyDTO> dtos = new ArrayList<>();
    companies.forEach(c -> dtos.add(toDto(c)));
    return dtos;
  }

  public List<Company> toEntityList(List<CompanyDTO> companyDTOs) {
    List<Company> entityList = new ArrayList<>();
    companyDTOs.forEach(c -> entityList.add(toEntity(c)));
    return entityList;
  }
}
