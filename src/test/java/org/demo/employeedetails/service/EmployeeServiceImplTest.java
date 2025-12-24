package org.demo.employeedetails.service;

import org.demo.employeedetails.domain.Employee;
import org.demo.employeedetails.dto.EmployeeDTO;
import org.demo.employeedetails.dto.EmployeeSearchCriteria;
import org.demo.employeedetails.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private Authentication auth;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee sample;

    @BeforeEach
    void setUp() {
        sample = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering", new BigDecimal("100000"), "123-45-6789");
    }

    @Test
    void search_withValidNames_returnsResults() {
        when(repository.findByFirstNameContainingIgnoreCase(ArgumentMatchers.anyString())).thenReturn(List.of(sample));
        when(auth.getAuthorities()).thenReturn(Collections.emptyList());

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria("John", null);
        List<EmployeeDTO> result = service.search(criteria, auth);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void search_withInvalidCharacters_throws() {
        when(auth.getAuthorities()).thenReturn(Collections.emptyList());
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria("John123", null);

        assertThatThrownBy(() -> service.search(criteria, auth))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("firstName contains invalid characters");
    }

    @Test
    void search_withTooLongName_throws() {
        when(auth.getAuthorities()).thenReturn(Collections.emptyList());
        String longName = "a".repeat(51);
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria(longName, null);

        assertThatThrownBy(() -> service.search(criteria, auth))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("firstName must be at most");
    }
}

