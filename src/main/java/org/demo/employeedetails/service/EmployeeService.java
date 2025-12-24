package org.demo.employeedetails.service;

import org.demo.employeedetails.domain.Employee;
import org.demo.employeedetails.dto.EmployeeDTO;
import org.demo.employeedetails.dto.EmployeeSearchCriteria;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> search(EmployeeSearchCriteria criteria, Authentication auth);

}

