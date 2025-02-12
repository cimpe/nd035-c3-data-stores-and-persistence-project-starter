package com.udacity.jdnd.course3.critter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeDTOToEmployeeConverter employeeDTOToEmployeeConverter;

    private final EmployeeToEmployeeDTOConverter employeeToEmployeeDTOConverter;

    public EmployeeDTO findById(final Long id) {
        return employeeRepository.findById(id)
                .map(employeeToEmployeeDTOConverter::convert)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public EmployeeDTO save(final EmployeeDTO employee) {
        final Employee toSaveEmployee = employeeDTOToEmployeeConverter.convert(employee);

        if (toSaveEmployee.getId() != null) {
            employeeRepository.findById(toSaveEmployee.getId())
                    .orElseThrow(EmployeeNotFoundException::new);
        }

        final Employee savedEmployee = employeeRepository.save(toSaveEmployee);

        return employeeToEmployeeDTOConverter.convert(savedEmployee);
    }

    public void setEmployeeAvailability(final Long id, final Set<DayOfWeek> daysAvailable) {
        final Employee employee = employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);

        employee.setDaysAvailable(daysAvailable);

        employeeRepository.save(employee);
    }

    public List<EmployeeDTO> findEmployeesForService(final EmployeeRequestDTO employeeDTO) {
        return employeeRepository.findAllByDaysAvailable(employeeDTO.getDate().getDayOfWeek()).stream()
                .filter(e -> e.getSkills().containsAll(employeeDTO.getSkills()))
                .map(employeeToEmployeeDTOConverter::convert)
                .collect(Collectors.toList());
    }

}
