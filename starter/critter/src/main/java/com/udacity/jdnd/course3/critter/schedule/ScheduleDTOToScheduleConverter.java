package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
final class ScheduleDTOToScheduleConverter implements Converter<ScheduleDTO, Schedule> {

    @Override
    public Schedule convert(final ScheduleDTO scheduleDTO) {
        final Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        schedule.setEmployees(scheduleDTO.getEmployeeIds().stream()
                .map(id -> {
                    final Employee employee = new Employee();
                    employee.setId(id);
                    return employee;
                })
                .collect(Collectors.toList()));

        schedule.setPets(scheduleDTO.getPetIds().stream()
                .map(id -> {
                    final Pet Pet = new Pet();
                    Pet.setId(id);
                    return Pet;
                })
                .collect(Collectors.toList()));

        return schedule;
    }

}
