package org.demo.employeedetails.service;

import org.demo.employeedetails.domain.Employee;
import org.demo.employeedetails.dto.EmployeeDTO;
import org.demo.employeedetails.dto.EmployeeSearchCriteria;
import org.demo.employeedetails.repository.EmployeeRepository;
import org.demo.employeedetails.util.NameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<EmployeeDTO> search(EmployeeSearchCriteria criteria, Authentication auth) {
        boolean isManager = hasRole(auth, "ROLE_MANAGER");
        log.info(" calling the service for employee details and is manager role {}" ,isManager);
        String first = criteria.getFirstName();
        String last = criteria.getLastName();

        // trim and validate inputs when provided
        if (first != null) {
            first = first.trim();
            if (!first.isEmpty()) {
                NameValidator.validate("firstName", first);
            }
        }
        if (last != null) {
            last = last.trim();
            if (!last.isEmpty()) {
                NameValidator.validate("lastName", last);
            }
        }

        // choose repository method based on provided criteria
        List<Employee> results;
        boolean firstBlank = (first == null || first.isBlank());
        boolean lastBlank = (last == null || last.isBlank());

        if (firstBlank && lastBlank) {
            results = employeeRepository.findAll();
        } else if (!firstBlank && lastBlank) {
            results = employeeRepository.findByFirstNameContainingIgnoreCase(first);
        } else if (firstBlank) {
            results = employeeRepository.findByLastNameContainingIgnoreCase(last);
        } else {
            results = employeeRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(first, last);
        }

        return results.stream().map(e -> mapToDto(e, isManager)).collect(Collectors.toList());
    }



    private boolean hasRole(Authentication auth, String role) {
        if (auth == null) return false;
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (role.equals(ga.getAuthority())) return true;
        }
        return false;
    }

    private EmployeeDTO mapToDto(Employee e, boolean includeSensitive) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setEmail(e.getEmail());
        dto.setDepartment(e.getDepartment());
        if (includeSensitive) {
            dto.setSalary(e.getSalary());
            dto.setSsn(maskSsn(e.getSsn()));
        } else {
            dto.setSalary(null);
            dto.setSsn(null);
        }
        return dto;
    }

    private String maskSsn(String ssn) {
        if (ssn == null) return null;
        if (ssn.length() <= 4) return "****";
        String last4 = ssn.substring(ssn.length() - 4);
        return "***-**-" + last4;
    }

}
