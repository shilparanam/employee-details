package org.demo.employeedetails.controller;

import org.demo.employeedetails.dto.EmployeeDTO;
import org.demo.employeedetails.dto.EmployeeSearchCriteria;
import org.demo.employeedetails.service.EmployeeService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")

public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeService employeeService;

    /*
    Endpoint to search for employees based on first name and/or last name.
    Both parameters are optional; if none are provided, all employees are returned.
    The method logs the search criteria and delegates the search to the EmployeeService.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> search(@RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName,
                                                    Authentication authentication) {
        log.info(" Employee details for  {} {}",firstName ,lastName);
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria(firstName, lastName);
        List<EmployeeDTO> results = employeeService.search(criteria, authentication);
        return ResponseEntity.ok(results);
    }
}

