package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
final class EmployeeToEmployeeDTOConverter implements Converter<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO convert(final Employee employee) {
        final EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);

        return employeeDTO;
    }

}
