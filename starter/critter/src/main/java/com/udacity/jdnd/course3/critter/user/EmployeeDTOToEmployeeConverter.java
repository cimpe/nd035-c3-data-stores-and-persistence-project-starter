package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Set;

@Component
final class EmployeeDTOToEmployeeConverter implements Converter<EmployeeDTO, Employee> {

    @Override
    public Employee convert(final EmployeeDTO employeeDTO) {
        final Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee, "id");

        final Set<EmployeeSkill> skills = employeeDTO.getSkills();
        if (skills != null) {
            skills.forEach(employee.getSkills()::add);
        }

        final Set<DayOfWeek> daysAvailable = employeeDTO.getDaysAvailable();
        if (daysAvailable != null) {
            daysAvailable.forEach(employee.getDaysAvailable()::add);
        }

        return employee;
    }

}
