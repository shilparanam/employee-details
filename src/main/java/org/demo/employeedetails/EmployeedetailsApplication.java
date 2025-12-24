package org.demo.employeedetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.demo.employeedetails.domain")
@EnableJpaRepositories(basePackages = "org.demo.employeedetails.repository")
public class EmployeedetailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeedetailsApplication.class, args);
    }

}
