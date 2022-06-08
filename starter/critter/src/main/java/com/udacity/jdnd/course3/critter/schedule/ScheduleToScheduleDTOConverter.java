package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
final class ScheduleToScheduleDTOConverter implements Converter<Schedule, ScheduleDTO> {

    @Override
    public ScheduleDTO convert(final Schedule schedule) {
        final ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList()));

        scheduleDTO.setPetIds(schedule.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList()));

        return scheduleDTO;
    }

}
